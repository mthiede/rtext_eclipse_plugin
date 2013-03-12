package org.rtext.lang.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;
import org.rtext.lang.util.Strings;

public class MultiLineTerminalsEditStrategy extends AbstractEditStrategy {
	
	private static final String SPACES = "  ";


	public static MultiLineTerminalsEditStrategy create(String indentationString, String leftTerminal, String rightTerminal) {
		return new MultiLineTerminalsEditStrategy(leftTerminal, indentationString, rightTerminal);
	}

	private String indentationString;
	

	public MultiLineTerminalsEditStrategy(String leftTerminal, String indentationString, String rightTerminal) {
		super(leftTerminal,rightTerminal);
		this.indentationString = indentationString;
	}

	@Override
	protected void internalCustomizeDocumentCommand(IDocument document, DocumentCommand command)
			throws BadLocationException {
		if (command.length != 0)
			return;
		String originalText = command.text;
		String[] lineDelimiters = document.getLegalLineDelimiters();
		int delimiterIndex = TextUtilities.startsWith(lineDelimiters, originalText);
		if (delimiterIndex != -1) {
			IRegion startTerminal = findStartTerminal(document, command.offset);
			if (startTerminal == null)
				return;
			IRegion stopTerminal = findStopTerminal(document, command.offset);
			// check whether this is our stop terminal
			if (stopTerminal != null) {
				IRegion previousStart = startTerminal;
				IRegion previousStop = stopTerminal;
				while(stopTerminal != null && previousStart != null && previousStop != null) {
					previousStart = findStartTerminal(document, previousStart.getOffset() - 1);
					if (previousStart != null) {
						previousStop = findStopTerminal(document, previousStop.getOffset() + 1);
						if (previousStop == null) {
							stopTerminal = null;
						}
					}
				}
			}
			if (util.isSameLine(document, startTerminal.getOffset(), command.offset)) {
				if (stopTerminal != null && stopTerminal.getLength() < getRightTerminal().length())
					stopTerminal = null;
				CommandInfo newC = handleCursorInFirstLine(document, command, startTerminal, stopTerminal);
				if (newC != null)
					newC.modifyCommand(command);
				return;
			} else if (stopTerminal == null) {
				CommandInfo newC = handleNoStopTerminal(document, command, startTerminal, stopTerminal);
				if (newC != null)
					newC.modifyCommand(command);
				return;
			} else if (!util.isSameLine(document, stopTerminal.getOffset(), command.offset)) {
				CommandInfo newC = handleCursorBetweenStartAndStopLine(document, command, startTerminal, stopTerminal);
				if (newC != null)
					newC.modifyCommand(command);
				return;
			} else {
				CommandInfo newC = handleCursorInStopLine(document, command, startTerminal, stopTerminal);
				if (newC != null)
					newC.modifyCommand(command);
				return;
			}
		}
	}

	/**
	 * Expects the cursor to be in the same line as the start terminal
	 * puts any text between start terminal and cursor into a separate newline before the cursor.
	 * puts any text between cursor and end terminal into a separate newline after the cursor.
	 * puts the closing terminal into a separate line at the end.
	 * adds a closing terminal if not existent.
	 */
	protected CommandInfo handleCursorInFirstLine(IDocument document, DocumentCommand command, IRegion startTerminal,
			IRegion stopTerminal) throws BadLocationException {
		CommandInfo newC = new CommandInfo();
		newC.isChange = true;
		newC.offset = command.offset;
		if(command.text.endsWith(SPACES)){
			newC.text += command.text + SPACES;
		}else{
			newC.text += command.text + indentationString;
		}
		newC.cursorOffset = command.offset + newC.text.length();
		if (stopTerminal == null && atEndOfLineInput(document, command.offset)) {
			newC.text += command.text + getRightTerminal();
		}
		if (stopTerminal != null && stopTerminal.getOffset() >= command.offset && util.isSameLine(document, stopTerminal.getOffset(), command.offset)) {
			String string = document.get(command.offset, stopTerminal.getOffset() - command.offset);
			if (string.trim().length() > 0)
				newC.text += string.trim();
			newC.text += command.text;
			newC.length += string.length();
		}
		return newC;
	}

	/**
	 * Expects the cursor not to be in the first line of the block
	 * inserts a closing terminal if not existent.
	 */
	protected CommandInfo handleNoStopTerminal(IDocument document, DocumentCommand command, IRegion startTerminal,
			IRegion stopTerminal) throws BadLocationException {
		if (atEndOfLineInput(document, command.offset)) {
			CommandInfo newC = new CommandInfo();
			newC.isChange = true;
			String textPartBeforeCursor = command.text + Strings.removeLeadingWhitespace(indentationString);
			newC.cursorOffset = command.offset+textPartBeforeCursor.length();
			newC.text = textPartBeforeCursor + command.text + Strings.removeLeadingWhitespace(getRightTerminal());
			return newC;
		}
		return null;
	}

	/**
	 * adds a new line with required indentation after the cursor.
	 */
	protected CommandInfo handleCursorBetweenStartAndStopLine(IDocument document, DocumentCommand command,
			IRegion startTerminal, IRegion stopTerminal) throws BadLocationException {
		if (indentationString.trim().length()>0 && !atBeginningOfLineInput(document, command.offset)) {
			CommandInfo newC = new CommandInfo();
			newC.isChange = true;
			newC.text += command.text + Strings.removeLeadingWhitespace(indentationString);
			return newC;
		}
		return null;
	}

	/**
	 * delegates to {@link #handleCursorBetweenStartAndStopLine(IDocument, DocumentCommand, IRegion, IRegion)}.
	 */
	protected CommandInfo handleCursorInStopLine(IDocument document, DocumentCommand command, IRegion startTerminal,
			IRegion stopTerminal) throws BadLocationException {
		return handleCursorBetweenStartAndStopLine(document, command, startTerminal, stopTerminal);
	}

	/**
	 * determines whether the given offset is at the beginning of the input in the line. leading whitespace is disregarded.
	 */
	private boolean atBeginningOfLineInput(IDocument document, int offset) throws BadLocationException {
		IRegion line = document.getLineInformation(document.getLineOfOffset(offset));
		return document.get(line.getOffset(), offset - line.getOffset()).trim().length() == 0;
	}

	/**
	 * determines whether the given offset is at the end of the input in the line. trailing whitespace is disregarded.
	 */
	protected boolean atEndOfLineInput(IDocument document, int offset) throws BadLocationException {
		IRegion line = document.getLineInformation(document.getLineOfOffset(offset));
		return document.get(offset, line.getOffset() + line.getLength() - offset).trim().length() == 0;
	}

}