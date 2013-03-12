/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;

public class CompoundMultiLineTerminalsEditStrategy implements IAutoEditStrategy {
	
	public static CompoundMultiLineTerminalsEditStrategy newInstanceFor(String indentationString, String leftTerminal, String rightTerminal) {
		CompoundMultiLineTerminalsEditStrategy strategy = new CompoundMultiLineTerminalsEditStrategy();
		return strategy.and(indentationString, leftTerminal, rightTerminal);
	}

	
	private List<MultiLineTerminalsEditStrategy> strategies;
	
	protected CompoundMultiLineTerminalsEditStrategy() {
		this.strategies = new ArrayList<MultiLineTerminalsEditStrategy>();
	}
	
	public CompoundMultiLineTerminalsEditStrategy and(String indentationString, String leftTerminal, String rightTerminal) {
		strategies.add(MultiLineTerminalsEditStrategy.create(indentationString, leftTerminal, rightTerminal));
		return this;
	}
	
	public CompoundMultiLineTerminalsEditStrategy and(String leftTerminal, String rightTerminal) {
		return and(leftTerminal, null, rightTerminal);
	}

	/**
	 * @since 2.3
	 */
	public CompoundMultiLineTerminalsEditStrategy and(MultiLineTerminalsEditStrategy strategy) {
		strategies.add(strategy);
		return this;
	}
	
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		if (command.length != 0)
			return;
		try{
		String[] lineDelimiters = document.getLegalLineDelimiters();
		int delimiterIndex = TextUtilities.startsWith(lineDelimiters, command.text);
		if (delimiterIndex != -1) {
			MultiLineTerminalsEditStrategy bestStrategy = null;
			IRegion bestStart = null;
			for(MultiLineTerminalsEditStrategy strategy: strategies) {
				IRegion candidate = strategy.findStartTerminal(document, command.offset);
				if (candidate != null) {
					if (bestStart == null || bestStart.getOffset() < candidate.getOffset()) {
						bestStrategy = strategy;
						bestStart = candidate;
					}
				}
			}
			if (bestStrategy != null) {
				bestStrategy.internalCustomizeDocumentCommand(document, command);
			}
		}
		}catch(BadLocationException e){
			return;
		}
	}


}