package org.rtext.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.rtext.model.RootElement;

public class RTextDocument extends Document {
		
	private RootElement model = new RootElement();
	private ReadWriteAcces<RootElement> documentAccess = createReadWriteAccess();
	private List<ModelChangeListener> listeners = new ArrayList<ModelChangeListener>();
	
	public RootElement getModel() {
		return model;
	}

	protected ReadWriteAcces<RootElement> createReadWriteAccess() {
		return new ReadWriteAcces<RootElement>() {

			@Override
			protected RootElement getState() {
				return model;
			}
			
			@Override
			protected void afterModify(RootElement state, Object result,
					IUnitOfWork<?, RootElement> work) {
				updateListeners(state);
			}
			
			private void updateListeners(RootElement newState) {
				for (ModelChangeListener listener : listeners) {
					listener.handleModelChange(newState);
				}
			}
		};
	}
	
	public <T> T readOnly(IUnitOfWork<T, RootElement> work) {
		return documentAccess.readOnly(work);
	}

	public <T> T modify(IUnitOfWork<T, RootElement> work) {
		return documentAccess.modify(work);
	}

	public void removeModelListener(ModelChangeListener listener) {
		listeners.remove(listener);
	}
	
	public void addModelListener(ModelChangeListener listener){
		listeners.add(listener);
	}
	
}
