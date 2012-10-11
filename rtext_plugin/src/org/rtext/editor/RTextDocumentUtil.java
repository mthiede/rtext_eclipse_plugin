package org.rtext.editor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.projection.ProjectionDocument;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class RTextDocumentUtil {
	
	public static IRTextDocument get(Object ctx) {
		if (ctx instanceof IRTextDocument)
			return (IRTextDocument) ctx;
		if (ctx instanceof ProjectionDocument)
			return get(((ProjectionDocument) ctx).getMasterDocument());
		if (ctx instanceof ITextViewer)
			return get(((ITextViewer) ctx).getDocument());
		if (ctx instanceof RTextEditor)
			return ((RTextEditor) ctx).getDocument();
		if (ctx instanceof IFile) {
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			return get(activePage.findEditor(new FileEditorInput((IFile) ctx)));
		}
		return null;
	}
}