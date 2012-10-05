package org.rtext.model;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetterOrDigit;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.rtext.editor.ColorManager;

public abstract class AbstractRTextParser<T> {

	public static final char EOL = (char)0;

	protected ColorManager fColorManager;
	private IDocument fDocument;
	private int fOffset;
	private int fTokenStart;
	private int fEndOffset;
	private boolean fNewLine;

	public int getTokenLength() {
		return fOffset - fTokenStart;
	}

	public int getTokenOffset() {
		return fTokenStart;
	}

	public T nextToken() {
		if (fOffset >= fEndOffset-1) {
			return createEndOfFile();
		}
		T result;
		tokenStart();
		consumeWhitespace();
		if (nextIsChar('#')) {
			consumeUntilEOL();
			result = createComment();
		}
		else if (nextIsWordStartCharacter()) {
			consumeWord();
			if (consumeChar(':')) {
				result = createLabel();
			}
			else if (consumeChar('/')) {
				consumeWord();
				while (consumeChar('/')) {
					consumeWord();
				}
				result = createReference();
			}
			else {
				if (fNewLine) {
					result = createCommand();
				}
				else {
					result = createIdentifier();
				}
			}
		}
		else if (nextIsDigit())	{
			consumeNumber();
			result = createNumber();
		}
		else if (consumeChar('/')) {
			consumeWord();
			while (consumeChar('/')) {
				consumeWord();
			}
			result = createReference();
		}
		else if (nextIsChar('"')) {
			consumeString();
			result = createString();
		}
		else {
			if (nextIsChar('{')) {
				openBlock();
			}
			else if (nextIsChar('}')) {
				closeBlock();
			}
			consumeAnyChar();
			result = createDefault();
		}
		fNewLine = false;
		return result;
	}

	protected void openBlock() {
	}

	protected void closeBlock() {
	}

	protected abstract T createEndOfFile();

	private void consumeNumber() {
		consumeDigits();
		consumeChar('.');
		consumeDigits();
	}

	private void consumeDigits() {
		while (isDigit(readChar()));
		unreadChar();
	}

	private boolean consumeWord() {
		boolean result = false;
		char c = readChar();
		while (isLetterOrDigit(c) || c == '_') {
			c = readChar();
			result = true;
		}
		unreadChar();
		return result;
	}

	private boolean consumeAnyChar() {
		readChar();
		return true;
	}

	private boolean consumeChar(char c) {
		if (readChar() == c) {
			return true;
		}
		else {
			unreadChar();
			return false;
		}
	}

	private void consumeString() {
		consumeChar('"');
		char c = readChar();
		while (c != '"' && c != EOL) {
			c = readChar();
		}
		unreadChar();
		consumeChar('"');
	}

	private boolean consumeWhitespace() {
		boolean result = false;
		char c = readChar();
		while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
			if (c == '\n') {
				fNewLine = true;
			}
			c = readChar();
			result = true;
		}
		unreadChar();
		return result;
	}

	private void consumeUntilEOL() {
		char c = readChar();
		while (c != '\n' && c != EOL) {
			c = readChar();
		}
		unreadChar();
	}

	private boolean nextIsChar(char c) {
		boolean result = (readChar() == c);
		unreadChar();
		return result;
	}

	private boolean nextIsWordStartCharacter() {
		char c = readChar();
		boolean result = (Character.isLetter(c) || c == '_');
		unreadChar();
		return result;
	}

	private boolean nextIsDigit() {
		boolean result = Character.isDigit(readChar());
		unreadChar();
		return result;
	}

	private void unreadChar() {
		if (fOffset < fEndOffset) {
			fOffset--;
		}
	}

	private char readChar() {
		char result = EOL;
		try {
			if (fOffset < fEndOffset) {
				result = fDocument.getChar(fOffset++);
			}
		} catch (BadLocationException e) {
		}
		return result;
	}
	
	protected String get(int offset, int length){
		try {
			return fDocument.get(offset, length);
		} catch (BadLocationException e) {
			return "";
		}
	}

	private void tokenStart() {
		fTokenStart = fOffset;
	}

	public void setRange(IDocument document, int offset, int length) {
		fNewLine = true;
		fDocument = document;
		fOffset = offset;
		fEndOffset = fOffset + length;
	}
	
	protected abstract T createComment();

	protected abstract T createLabel();

	protected abstract T createReference();

	protected abstract T createCommand();

	protected abstract T createIdentifier();

	protected abstract T createNumber();

	protected abstract T createString();

	protected abstract T createDefault();

}