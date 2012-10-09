package org.rtext.editor;

import static org.rtext.util.Expectations.expectType;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.rtext.model.RTextModelParser;
import org.rtext.model.RTextResource;
public class RTextReconcilingStrategy implements IReconcilingStrategy {

	private RTextDocument document;
	private RTextModelParser modelParser = new RTextModelParser();

	@Override
	public void setDocument(IDocument document) {
		expectType(RTextDocument.class, document);
		this.document = (RTextDocument) document;
		reconcile(new Region(0, document.getLength()));
	}
	
	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		// not implemented
	}

	@Override
	public void reconcile(IRegion partition) {
		document.modify(new IUnitOfWork.Void<RTextResource>(){
			@Override
			public void process(RTextResource state) throws Exception {
				modelParser.setRange(document, 0, document.getLength());
				state.getContents().clear();
				state.getContents().addAll(modelParser.parse());
			}
		});
	}

}
