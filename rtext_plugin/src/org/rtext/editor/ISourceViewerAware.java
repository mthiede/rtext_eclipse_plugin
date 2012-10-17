package org.rtext.editor;

import org.eclipse.jface.text.source.ISourceViewer;

public interface ISourceViewerAware {
	void setSourceViewer(ISourceViewer sourceViewer);
}
