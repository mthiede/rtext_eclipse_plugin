/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.rtext.lang.model.RTextResource;

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
	
}
