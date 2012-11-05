/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

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
