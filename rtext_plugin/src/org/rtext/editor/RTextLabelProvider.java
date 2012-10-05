package org.rtext.editor;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.rtext.model.Element;

public class RTextLabelProvider extends LabelProvider {
	
	private PluginImageHelper imageHelper;
	
	public RTextLabelProvider(){
		this(new PluginImageHelper());
	}

	public RTextLabelProvider(PluginImageHelper imageHelper) {
		this.imageHelper = imageHelper;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Element) {
			return ((Element) element).getName();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		return imageHelper.getImage("element.gif");
	}

}
