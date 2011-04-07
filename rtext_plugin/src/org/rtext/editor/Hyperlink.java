package org.rtext.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IWorkbenchPage;

public class Hyperlink implements IHyperlink {
	private IWorkbenchPage workbenchPage;
	private IRegion region;
	private String filename;
	private int line;
	private String text;
	
	public Hyperlink(IWorkbenchPage workbenchPart, IRegion region, String filename, int line, String text) {
		this.workbenchPage = workbenchPart;
		this.region = region;
		this.filename = filename;
		this.line = line;
		this.text = text;
	}
	
	public IRegion getHyperlinkRegion() {
		return region;
	}

	public String getHyperlinkText() {
		return text;
	}

	public String getTypeLabel() {
		return null;
	}

	public void open() {
		OpenEditorHelper.openInRTextEditor(workbenchPage, filename, line);
	}

}
