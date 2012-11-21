/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import static org.eclipse.ui.texteditor.MarkerUtilities.getMessage;

import java.util.Iterator;

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
		StringBuilder result = new StringBuilder();
		while (it.hasNext()) {
			Annotation annotation = (Annotation)it.next();
			if (annotation instanceof MarkerAnnotation) {
				MarkerAnnotation markerAnnotation = (MarkerAnnotation)annotation;
				try {
					int annotationLineNumber = sourceViewer.getDocument().getLineOfOffset(annotationModel.getPosition(annotation).getOffset());
					if (annotationLineNumber == lineNumber) {
						result.append(getMessage(markerAnnotation.getMarker()) + "\n");
					}
				} catch (BadLocationException e) {
				}
			}
		}
		return result.toString();
	}
	
}
