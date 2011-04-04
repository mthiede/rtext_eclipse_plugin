package org.rtext.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;

public class SyntaxScanner implements ITokenScanner {

	private ColorManager fColorManager;
	private IDocument fDocument;
	private int fOffset;
	private int fTokenLength;
	private int fTokenStart;
	private int fEndOffset;
	private boolean fNewLine;

	public SyntaxScanner(ColorManager manager) {
		fColorManager = manager;
	}

	public int getTokenLength() {
		return fTokenLength;
	}

	public int getTokenOffset() {
		return fTokenStart;
	}

	public IToken nextToken() {
		if (fOffset >= fEndOffset-1) {
			return Token.EOF;
		}
		IToken result;
		tokenStart();
		consumeWhitespace();
		if (nextIsChar('#')) {
			consumeUntilEOL();
			result = createToken(IColorConstants.COMMENT);
		}
		else if (nextIsWordStartCharacter()) {
			consumeWord();
			if (consumeChar(':')) {
				result = createToken(IColorConstants.LABEL);
			}
			else if (consumeChar('/')) {
				consumeWord();
				while (consumeChar('/')) {
					consumeWord();
				}
				result = createToken(IColorConstants.REFERENCE);
			}
			else {
				if (fNewLine) {
					result = createToken(IColorConstants.COMMAND);
				}
				else {
					result = createToken(IColorConstants.IDENTIFIER);
				}
			}
		}
		else if (nextIsDigit())	{
			consumeNumber();
			result = createToken(IColorConstants.NUMBER);
		}
		else if (consumeChar('/')) {
			consumeWord();
			while (consumeChar('/')) {
				consumeWord();
			}
			result = createToken(IColorConstants.REFERENCE);
		}
		else if (nextIsChar('"')) {
			consumeString();
			result = createToken(IColorConstants.STRING);
		}
		else {
			consumeAnyChar();
			result = createToken(IColorConstants.DEFAULT);
		}
		fNewLine = false;
		return result;
	}

	private void consumeNumber() {
		consumeDigits();
		consumeChar('.');
		consumeDigits();
	}

	private void consumeDigits() {
		while (Character.isDigit(readChar()));
		unreadChar();
	}

	private boolean consumeWord() {
		boolean result = false;
		char c = readChar();
		while (Character.isLetterOrDigit(c) || c == '_') {
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
		while (c != '"' && c != (char)0) {
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
		while (c != '\n' && c != (char)0) {
			c = readChar();
		}
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
		char result = (char)0;
		try {
			if (fOffset < fEndOffset) {
				result = fDocument.getChar(fOffset++);
			}
		} catch (BadLocationException e) {
		}
		return result;
	}


	private IToken createToken(RGB rgb) {
		fTokenLength = fOffset - fTokenStart;
		return new Token(new TextAttribute(fColorManager.getColor(rgb)));
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
}
