/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Position;
import org.rtext.lang.model.ModelChangeListener;
import org.rtext.lang.model.RTextResource;
import org.rtext.lang.util.IUnitOfWork;
import org.rtext.lang.util.ReadWriteAcces;

public class RTextDocument extends Document implements IRTextDocument {
		
	private RTextResource model = new RTextResource();
	private ReadWriteAcces<RTextResource> documentAccess = createReadWriteAccess();
	private List<ModelChangeListener> listeners = new ArrayList<ModelChangeListener>();
	
	public RTextResource getModel() {
		return model;
	}

	protected ReadWriteAcces<RTextResource> createReadWriteAccess() {
		return new ReadWriteAcces<RTextResource>() {

			@Override
			protected RTextResource getState() {
				return model;
			}
			
			@Override
			protected void afterModify(RTextResource state, Object result,
					IUnitOfWork<?, RTextResource> work) {
				updateListeners(state);
			}
			
			private void updateListeners(RTextResource newState) {
				for (ModelChangeListener listener : listeners) {
					listener.handleModelChange(newState);
				}
			}
		};
	}
	
	public <T> T readOnly(IUnitOfWork<T, RTextResource> work) {
		return documentAccess.readOnly(work);
	}

	public <T> T modify(IUnitOfWork<T, RTextResource> work) {
		return documentAccess.modify(work);
	}

	public void removeModelListener(ModelChangeListener listener) {
		listeners.remove(listener);
	}
	
	public void addModelListener(ModelChangeListener listener){
		listeners.add(listener);
	}
	
	/*
	 * fix for https://bugs.eclipse.org/bugs/show_bug.cgi?id=297946
	 */
	private ReadWriteLock positionsLock = new ReentrantReadWriteLock();
	private Lock positionsReadLock = positionsLock.readLock();
	private Lock positionsWriteLock = positionsLock.writeLock();

	@Override
	public Position[] getPositions(String category, int offset, int length, boolean canStartBefore, boolean canEndAfter)
			throws BadPositionCategoryException {
		positionsReadLock.lock();
		try {
			return super.getPositions(category, offset, length, canStartBefore, canEndAfter);
		} finally {
			positionsReadLock.unlock();
		}
	}

	@Override
	public Position[] getPositions(String category) throws BadPositionCategoryException {
		positionsReadLock.lock();
		try {
			return super.getPositions(category);
		} finally {
			positionsReadLock.unlock();
		}
	}

	@Override
	public void addPosition(Position position) throws BadLocationException {
		positionsWriteLock.lock();
		try {
			super.addPosition(position);
		} finally {
			positionsWriteLock.unlock();
		}
	}

	@Override
	public void addPosition(String category, Position position) throws BadLocationException,
			BadPositionCategoryException {
		positionsWriteLock.lock();
		try {
			super.addPosition(category, position);
		} finally {
			positionsWriteLock.unlock();
		}
	}

	@Override
	public void removePosition(Position position) {
		positionsWriteLock.lock();
		try {
			super.removePosition(position);
		} finally {
			positionsWriteLock.unlock();
		}
	}

	@Override
	public void removePosition(String category, Position position) throws BadPositionCategoryException {
		positionsWriteLock.lock();
		try {
			super.removePosition(category, position);
		} finally {
			positionsWriteLock.unlock();
		}
	}
}
