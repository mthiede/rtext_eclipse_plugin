/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.specs.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.rtext.lang.util.Exceptions;

/**
 * Minimal implementation of IDocument to apply text edit onto a string.
 */
public class SimpleDocument implements IDocument {

	private StringBuffer buffer;
	private ILineTracker tracker;

	public SimpleDocument(String source) {
		set(source);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getChar(int)
	 */
	public char getChar(int offset) {
		return buffer.charAt(offset);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLength()
	 */
	public int getLength() {
		return buffer.length();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#get()
	 */
	public String get() {
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#get(int, int)
	 */
	public String get(int offset, int length) {
		return buffer.substring(offset, offset + length);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#set(java.lang.String)
	 */
	public void set(String text) {
		buffer = new StringBuffer(text);
		tracker = new DefaultLineTracker();
		tracker.set(text);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#replace(int, int, java.lang.String)
	 */
	public void replace(int offset, int length, String text) {
		buffer.replace(offset, offset + length, text);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addDocumentListener(org.eclipse.jface.text.IDocumentListener)
	 */
	public void addDocumentListener(IDocumentListener listener) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removeDocumentListener(org.eclipse.jface.text.IDocumentListener)
	 */
	public void removeDocumentListener(IDocumentListener listener) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addPrenotifiedDocumentListener(org.eclipse.jface.text.IDocumentListener)
	 */
	public void addPrenotifiedDocumentListener(IDocumentListener documentAdapter) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removePrenotifiedDocumentListener(org.eclipse.jface.text.IDocumentListener)
	 */
	public void removePrenotifiedDocumentListener(IDocumentListener documentAdapter) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addPositionCategory(java.lang.String)
	 */
	public void addPositionCategory(String category) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removePositionCategory(java.lang.String)
	 */
	public void removePositionCategory(String category) {
			throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getPositionCategories()
	 */
	public String[] getPositionCategories() {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#containsPositionCategory(java.lang.String)
	 */
	public boolean containsPositionCategory(String category) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addPosition(org.eclipse.jface.text.Position)
	 */
	public void addPosition(Position position) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removePosition(org.eclipse.jface.text.Position)
	 */
	public void removePosition(Position position) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addPosition(java.lang.String, org.eclipse.jface.text.Position)
	 */
	public void addPosition(String category, Position position) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removePosition(java.lang.String, org.eclipse.jface.text.Position)
	 */
	public void removePosition(String category, Position position) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getPositions(java.lang.String)
	 */
	public Position[] getPositions(String category) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#containsPosition(java.lang.String, int, int)
	 */
	public boolean containsPosition(String category, int offset, int length) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#computeIndexInCategory(java.lang.String, int)
	 */
	public int computeIndexInCategory(String category, int offset) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addPositionUpdater(org.eclipse.jface.text.IPositionUpdater)
	 */
	public void addPositionUpdater(IPositionUpdater updater) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removePositionUpdater(org.eclipse.jface.text.IPositionUpdater)
	 */
	public void removePositionUpdater(IPositionUpdater updater) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#insertPositionUpdater(org.eclipse.jface.text.IPositionUpdater, int)
	 */
	public void insertPositionUpdater(IPositionUpdater updater, int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getPositionUpdaters()
	 */
	public IPositionUpdater[] getPositionUpdaters() {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLegalContentTypes()
	 */
	public String[] getLegalContentTypes() {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getContentType(int)
	 */
	public String getContentType(int offset) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getPartition(int)
	 */
	public ITypedRegion getPartition(int offset) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#computePartitioning(int, int)
	 */
	public ITypedRegion[] computePartitioning(int offset, int length) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#addDocumentPartitioningListener(org.eclipse.jface.text.IDocumentPartitioningListener)
	 */
	public void addDocumentPartitioningListener(IDocumentPartitioningListener listener) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#removeDocumentPartitioningListener(org.eclipse.jface.text.IDocumentPartitioningListener)
	 */
	public void removeDocumentPartitioningListener(IDocumentPartitioningListener listener) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#setDocumentPartitioner(org.eclipse.jface.text.IDocumentPartitioner)
	 */
	public void setDocumentPartitioner(IDocumentPartitioner partitioner) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getDocumentPartitioner()
	 */
	public IDocumentPartitioner getDocumentPartitioner() {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLineLength(int)
	 */
	public int getLineLength(int line) {
		try {
			return tracker.getLineLength(line);
		} catch (BadLocationException e) {
			Exceptions.rethrow(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLineOfOffset(int)
	 */
	public int getLineOfOffset(int offset) {
		try {
			return tracker.getLineNumberOfOffset(offset);
		} catch (BadLocationException e) {
			Exceptions.rethrow(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLineOffset(int)
	 */
	public int getLineOffset(int line) {
		try {
			return tracker.getLineOffset(line);
		} catch (BadLocationException e) {
			Exceptions.rethrow(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLineInformation(int)
	 */
	public IRegion getLineInformation(int line) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLineInformationOfOffset(int)
	 */
	public IRegion getLineInformationOfOffset(int offset) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getNumberOfLines()
	 */
	public int getNumberOfLines() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getNumberOfLines(int, int)
	 */
	public int getNumberOfLines(int offset, int length) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#computeNumberOfLines(java.lang.String)
	 */
	public int computeNumberOfLines(String text) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLegalLineDelimiters()
	 */
	public String[] getLegalLineDelimiters() {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IDocument#getLineDelimiter(int)
	 */
	public String getLineDelimiter(int line) {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * @see org.eclipse.jface.text.IDocument#search(int, java.lang.String, boolean, boolean, boolean)
	 * @deprecated
	 */
	public int search(
		int startOffset,
		String findString,
		boolean forwardSearch,
		boolean caseSensitive,
		boolean wholeWord) {
		throw new UnsupportedOperationException();
	}

}
