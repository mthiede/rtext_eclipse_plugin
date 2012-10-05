package org.rtext.editor;

import static org.rtext.util.Expectations.expectType;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.rtext.model.Element;
import org.rtext.model.RootElement;
public class RTextReconcilingStrategy implements IReconcilingStrategy {

	private RTextDocument document;

	@Override
	public void setDocument(IDocument document) {
		expectType(RTextDocument.class, document);
		this.document = (RTextDocument) document;
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		// not implemented
	}

	@Override
	public void reconcile(IRegion partition) {
		document.modify(new IUnitOfWork.Void<RootElement>(){
			@Override
			public void process(RootElement state) throws Exception {
				state.update(new Element("example " + System.currentTimeMillis(), state));
			}
		});
	}

}
