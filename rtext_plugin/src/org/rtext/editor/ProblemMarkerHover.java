package org.rtext.editor;

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.MarkerAnnotation;

public class ProblemMarkerHover implements IAnnotationHover {

	@Override
	public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
		IAnnotationModel annotationModel = sourceViewer.getAnnotationModel();
		Iterator<?> it = annotationModel.getAnnotationIterator();
		String result = null;
		while (it.hasNext()) {
			Annotation annotation = (Annotation)it.next();
			if (annotation instanceof MarkerAnnotation) {
				try {
					int annotationLineNumber = sourceViewer.getDocument().getLineOfOffset(annotationModel.getPosition(annotation).getOffset());
					if (annotationLineNumber == lineNumber) {
						if (result == null) {
							result = "";
						}
						String message = (String)((MarkerAnnotation)annotation).getMarker().getAttribute(IMarker.MESSAGE);
						result = result.concat(message);
					}
				} catch (BadLocationException e) {
				} catch (CoreException e) {
				}
			}
		}
		return result;
	}
	
}
