/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import static org.rtext.lang.util.Expectations.expectType;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.rtext.lang.model.RTextModelParser;
import org.rtext.lang.model.RTextResource;
public class RTextReconcilingStrategy implements IReconcilingStrategy {

	private RTextDocument document;
	private RTextModelParser modelParser = new RTextModelParser();

	public void setDocument(IDocument document) {
		expectType(RTextDocument.class, document);
		this.document = (RTextDocument) document;
		reconcile(new Region(0, document.getLength()));
	}
	
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		// not implemented
	}

	public void reconcile(final IRegion partition) {
		document.modify(new IUnitOfWork.Void<RTextResource>(){
			@Override
			public void process(RTextResource state) throws Exception {
				modelParser.setRange(document, 0, document.getLength());
				state.update(partition.getLength(), modelParser.parse());
			}
		});
	}

}
