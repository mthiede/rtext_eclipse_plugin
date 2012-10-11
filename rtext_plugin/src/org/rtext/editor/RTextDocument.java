package org.rtext.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.rtext.model.RTextResource;

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
	
	/* (non-Javadoc)
	 * @see org.rtext.editor.IRTextDocument#readOnly(org.rtext.editor.IUnitOfWork)
	 */
	@Override
	public <T> T readOnly(IUnitOfWork<T, RTextResource> work) {
		return documentAccess.readOnly(work);
	}

	/* (non-Javadoc)
	 * @see org.rtext.editor.IRTextDocument#modify(org.rtext.editor.IUnitOfWork)
	 */
	@Override
	public <T> T modify(IUnitOfWork<T, RTextResource> work) {
		return documentAccess.modify(work);
	}

	/* (non-Javadoc)
	 * @see org.rtext.editor.IRTextDocument#removeModelListener(org.rtext.editor.ModelChangeListener)
	 */
	@Override
	public void removeModelListener(ModelChangeListener listener) {
		listeners.remove(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.rtext.editor.IRTextDocument#addModelListener(org.rtext.editor.ModelChangeListener)
	 */
	@Override
	public void addModelListener(ModelChangeListener listener){
		listeners.add(listener);
	}
	
}
