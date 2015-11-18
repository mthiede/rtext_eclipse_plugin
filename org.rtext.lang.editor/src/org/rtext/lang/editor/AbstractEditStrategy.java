/*******************************************************************************
  * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.rtext.lang.document.DocumentUtil;

public abstract class AbstractEditStrategy  implements IAutoEditStrategy, VerifyKeyListener{

	private String leftTerminal;
	private String rightTerminal;
	private boolean skipNext = false;
	protected DocumentUtil util = new DocumentUtil();
	private static final int MAX_SIZE = 4000;
	
	protected abstract void internalCustomizeDocumentCommand(IDocument document,
			DocumentCommand command) throws BadLocationException;


	public String getLeftTerminal() {
		return leftTerminal;
	}

	public String getRightTerminal() {
		return rightTerminal;
	}

	public int count(String toFind, String searchMe) throws BadLocationException {
		int count = 0;
		int index = -toFind.length();
		while (true) {
			index = searchMe.indexOf(toFind, index + toFind.length());
			if (index == -1) {
				return count;
			} else {
				count++;
			}
		}
	}

	protected String getDocumentContent(IDocument document, DocumentCommand command)
			throws BadLocationException {
				return document.get();
			}

	public AbstractEditStrategy(String left, String right) {
		leftTerminal = left;
		rightTerminal = right;
	}

	private boolean shouldSkipNext(int keyCode) {
		IKeyLookup lookUp = KeyLookupFactory.getDefault();
		return lookUp.getCommand() == keyCode || lookUp.getCtrl() == keyCode;
	}

	public void verifyKey(VerifyEvent event) {
		skipNext = shouldSkipNext(event.keyCode);
	}

	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		if (skipNext)
			return;
		try {
			internalCustomizeDocumentCommand(document, command);
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * finds the first start terminal which is not closed before the cursor position.
	 */
	protected IRegion findStopTerminal(IDocument document, int offset) throws BadLocationException {
		int stopOffset = offset;
		int startOffset = offset;
		if (document.getNumberOfLines() > MAX_SIZE) {
			return new Region(0,1); // auto indentation works, auto closing brackets only when opening a new bracket for a new command
		}
		while (true) {
			IRegion stop = util.searchInSamePartition(getRightTerminal(), document, stopOffset);
			if (stop==null)
				return null;
			IRegion start = util.searchInSamePartition(getLeftTerminal(), document, startOffset);
			if (start==null || start.getOffset()>stop.getOffset()) {
				return stop;
			}
			stopOffset = util.findNextOffSetInPartition(document, stopOffset, stop.getOffset()+stop.getLength());
			startOffset = util.findNextOffSetInPartition(document, startOffset, start.getOffset()+start.getLength());
		}
	}

	/**
	 * finds the first stop terminal which has not been started after the cursor position.
	 */
	protected IRegion findStartTerminal(IDocument document, int offset) throws BadLocationException {
		int stopOffset = offset;
		int startOffset = offset;
		if (document.getNumberOfLines() > (MAX_SIZE * 2)) {
			return null; // this breaks auto-indentation of newlines (closing brackets will still be added when opening)
		}
		while(true) {
			IRegion start = util.searchBackwardsInSamePartition(getLeftTerminal(), document, startOffset);
			if (start==null)
				return null;
			IRegion stop = util.searchBackwardsInSamePartition(getRightTerminal(), document, stopOffset);
			if (stop == null || stop.getOffset()<start.getOffset())
				return start;
			stopOffset = stop.getOffset();
			startOffset = start.getOffset();
		}
	}

}