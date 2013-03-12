/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;


public class SingleLineTerminalsStrategy extends AbstractEditStrategy {


	public static interface StrategyPredicate {
		/**
		 * @return whether the closing terminal should be inserted, based on the cursor position
		 * @throws BadLocationException
		 *             exceptions are not thrown, thrown exceptions are catched and interpreted like return
		 *             <code>true</code>
		 */
		boolean isInsertClosingBracket(IDocument doc, int offset) throws BadLocationException;
	}

	public static StrategyPredicate DEFAULT = new StrategyPredicate() {
		public boolean isInsertClosingBracket(IDocument doc, int offset) throws BadLocationException {
			if (doc.getLength() <= offset)
				return true;
			char charAtOffset = doc.getChar(offset);
			boolean result = !(
					Character.isJavaIdentifierStart(charAtOffset)
					|| Character.isDigit(charAtOffset)
					|| charAtOffset == '!'
					|| charAtOffset == '-'
					|| charAtOffset == '('
					|| charAtOffset == '{'
					|| charAtOffset == '['
					|| charAtOffset == '\''
					|| charAtOffset == '\"');
			return result;
		}
	};
	
	private StrategyPredicate strategy;
	
	public static SingleLineTerminalsStrategy create(String left, String right){
		return new SingleLineTerminalsStrategy(left, right, DEFAULT);
	}

	public SingleLineTerminalsStrategy(String left, String right, StrategyPredicate strategy) {
		super(left, right);
		this.strategy = strategy;
	}

	@Override
	protected void internalCustomizeDocumentCommand(IDocument document, DocumentCommand command)
			throws BadLocationException {
		handleInsertLeftTerminal(document, command);
		handleInsertRightTerminal(document, command);
		handleDeletion(document, command);
	}

	protected void handleInsertLeftTerminal(IDocument document, DocumentCommand command) throws BadLocationException {
		if (command.text.length() > 0 && appliedText(document, command).endsWith(getLeftTerminal())
				&& isInsertClosingTerminal(document, command.offset + command.length)) {
			String documentContent = getDocumentContent(document, command);
			int opening = count(getLeftTerminal(), documentContent);
			int closing = count(getRightTerminal(), documentContent);
			int occurences = opening + closing;
			if (occurences % 2 == 0
					&& (command.text.length() - command.length + documentContent.length() >= getLeftTerminal().length())) {
				command.caretOffset = command.offset + command.text.length();
				command.text = command.text + getRightTerminal();
				command.shiftsCaret = false;
			}
		}
	}

	protected boolean isInsertClosingTerminal(IDocument document, int i) {
		try {
			return strategy.isInsertClosingBracket(document, i);
		} catch (BadLocationException e) {
			return true;
		}
	}

	protected String appliedText(IDocument document, DocumentCommand command) throws BadLocationException {
		String string = document.get(0, command.offset);
		return string + command.text;
	}

	protected void handleDeletion(IDocument document, DocumentCommand command) throws BadLocationException {
		if (command.text.equals("") && command.length == 1) {
			if (command.offset + getRightTerminal().length() + getLeftTerminal().length() > document.getLength())
				return;
			if (command.offset + command.length - getLeftTerminal().length() < 0)
				return;
			if (command.length != getLeftTerminal().length())
				return;
			String string = document.get(command.offset, getLeftTerminal().length() + getRightTerminal().length());
			if (string.equals(getLeftTerminal() + getRightTerminal())) {
				command.length = getLeftTerminal().length() + getRightTerminal().length();
			}
		}
	}

	protected void handleInsertRightTerminal(IDocument document, DocumentCommand command) throws BadLocationException {
		//closing terminal
		if (command.text.equals(getRightTerminal())) {
			if (command.offset + command.length + getRightTerminal().length() > document.getLength())
				return;
			String documentContent = getDocumentContent(document, command);
			int opening = count(getLeftTerminal(), documentContent);
			int closing = count(getRightTerminal(), documentContent);
			if (opening <= closing
					&& getRightTerminal().equals(document.get(command.offset + command.length, command.text.length()))) {
				command.length += getRightTerminal().length();
			}
		}
	}

}
