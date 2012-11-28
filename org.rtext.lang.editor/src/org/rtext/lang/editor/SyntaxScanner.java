/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;


import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.rtext.lang.model.AbstractRTextParser;

public class SyntaxScanner extends AbstractRTextParser<IToken> implements ITokenScanner {

	private ColorManager fColorManager;

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
		return createBoldToken(IColorConstants.COMMAND);
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
	
	@Override
	protected IToken createAnnotation() {
		return createToken(IColorConstants.ANNOTATION);
	}
	
	@Override
	protected IToken createGenerics() {
		return createToken(IColorConstants.GENERICS);
	}
	
	protected IToken createToken(RGB rgb) {
		return createToken(rgb, SWT.NORMAL);
	}
	
	protected IToken createBoldToken(RGB rgb) {
		return createToken(rgb, SWT.BOLD);
	}
	
	protected IToken createToken(RGB rgb, int style) {
		TextAttribute textAttribute = new TextAttribute(color(rgb), null, style);
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
