package rtext.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.RGB;

public class SyntaxScanner implements ITokenScanner {

	private static final int LINESTART = 0;
	private static final int COMMAND = 1;

	private ColorManager fColorManager;
	private IDocument fDocument;
	private int fOffset;
	private int fState;
	private int fTokenLength;
	private int fTokenStart;
	private int fEndOffset;

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
		switch (fState) {
		case LINESTART:
			consumeWhitespace();
			tokenStart();
			if (nextIsChar('#')) {
				consumeUntilEOL();
				return createToken(IColorConstants.COMMENT);
			}
			else {
				if (consumeWord() && nextIsWhitespace()) {
					fState = COMMAND;
					return createToken(IColorConstants.COMMAND);
				}
				else {
					consumeUntilEOL();
					return createToken(IColorConstants.DEFAULT);
				}
			}
		case COMMAND:
			consumeWhitespace();
			consumeChar(',');
			consumeWhitespace();
			tokenStart();
			if (nextIsChar('"')) {
				consumeString();
				return createToken(IColorConstants.STRING);
			}
			else if (nextIsDigit()) {
				consumeNumber();
				return createToken(IColorConstants.NUMBER);
			}
			else {
				if (consumeWord()) {
					if (consumeChar(':')) {
						return createToken(IColorConstants.LABEL);
					}
					else if (nextIsWhitespace() || nextIsChar(',')){
						return createToken(IColorConstants.IDENTIFIER);
					}
					else {
						consumeUntilEOL();
						fState = LINESTART;
						return createToken(IColorConstants.DEFAULT);
					}
				}
				else {
					consumeUntilEOL();
					fState = LINESTART;
					return createToken(IColorConstants.DEFAULT);
				}
			}
		default:
			break;
		}
		return createToken(IColorConstants.DEFAULT);		
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
		while (c == ' ' || c == '\t') {
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
	
	private boolean nextIsDigit() {
		boolean result = Character.isDigit(readChar());
		unreadChar();
		return result;
	}

	private boolean nextIsWhitespace() {
		char c = readChar();
		boolean result = (c == ' ' || c == '\t');
		unreadChar();
		return result;
	}
	
	private void unreadChar() {
		fOffset--;
	}

	private char readChar() {
		try {
			if (fOffset < fEndOffset) {
				return fDocument.getChar(fOffset++);
			}
		} catch (BadLocationException e) {
		}
		return (char)0;
	}


	private IToken createToken(RGB rgb) {
		fTokenLength = fOffset - fTokenStart;
		return new Token(new TextAttribute(fColorManager.getColor(rgb)));
	}

	private void tokenStart() {
		fTokenStart = fOffset;
	}

	public void setRange(IDocument document, int offset, int length) {
		fDocument = document;
		fOffset = offset;
		fEndOffset = fOffset + length;
	}
}
