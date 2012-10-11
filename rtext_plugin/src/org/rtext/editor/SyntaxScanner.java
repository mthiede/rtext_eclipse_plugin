package org.rtext.editor;


import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.rtext.model.AbstractRTextParser;

public class SyntaxScanner extends AbstractRTextParser<IToken> implements ITokenScanner {

	public SyntaxScanner(ColorManager manager) {
		fColorManager = manager;
	}

	@Override
	protected IToken createDefault() {
		return createToken(IColorConstants.DEFAULT);
	}

	@Override
	protected IToken createString() {
		return createToken(IColorConstants.STRING);
	}

	@Override
	protected IToken createNumber() {
		return createToken(IColorConstants.NUMBER);
	}

	@Override
	protected IToken createIdentifier() {
		return createToken(IColorConstants.IDENTIFIER);
	}

	@Override
	protected IToken createCommand() {
		return createToken(IColorConstants.COMMAND);
	}

	@Override
	protected IToken createReference() {
		return createToken(IColorConstants.REFERENCE);
	}

	@Override
	protected IToken createLabel() {
		return createToken(IColorConstants.LABEL);
	}

	@Override
	protected IToken createComment() {
		return createToken(IColorConstants.COMMENT);
	}
	
	protected IToken createToken(RGB rgb) {
		int style = SWT.NORMAL;
		if(rgb == IColorConstants.COMMAND){
			style = SWT.BOLD;
		}
		TextAttribute textAttribute = new TextAttribute(color(rgb), color(new RGB(255, 255, 255)), style);
		return new Token(textAttribute);
	}

	private Color color(RGB rgb) {
		return fColorManager.getColor(rgb);
	}

	@Override
	protected IToken createEndOfFile() {
		return Token.EOF;
	}
	
}
