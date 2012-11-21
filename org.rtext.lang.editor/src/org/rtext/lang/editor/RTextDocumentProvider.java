/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;

public class RTextDocumentProvider extends FileDocumentProvider {

	protected IDocument createEmptyDocument() {
		return new RTextDocument();
	}
	
	@Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) element;
			return new ResourceMarkerAnnotationModel(input.getFile());
		} else if(element instanceof IURIEditorInput){
			return new AnnotationModel();
		}
		return super.createAnnotationModel(element);
	}
		
}
