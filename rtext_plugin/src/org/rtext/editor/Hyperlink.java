package org.rtext.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IWorkbenchPage;

public class Hyperlink implements IHyperlink {
	private IWorkbenchPage workbenchPage;
	private IRegion region;
	private String filename;
	private int line;
	
	public Hyperlink(IWorkbenchPage workbenchPart, IRegion region, String filename, int line) {
		this.workbenchPage = workbenchPart;
		this.region = region;
		this.filename = filename;
		this.line = line;
	}
	
	public IRegion getHyperlinkRegion() {
		return region;
	}

	public String getHyperlinkText() {
		return null;
	}

	public String getTypeLabel() {
		return null;
	}

	public void open() {
		OpenEditorHelper.openInRTextEditor(workbenchPage, filename, line);
	}

}
