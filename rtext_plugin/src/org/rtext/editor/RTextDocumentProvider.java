package org.rtext.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class RTextDocumentProvider extends FileDocumentProvider {

	protected IDocument createEmptyDocument() {
		return new RTextDocument();
	}
	
		
}
