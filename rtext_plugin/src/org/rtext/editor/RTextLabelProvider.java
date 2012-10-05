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
	public String getText(Object obj) {
		if (!(obj instanceof Element)) {
			return super.getText(obj);
		}
		Element element = (Element) obj;
		String name = element.getName();
		if(name != null && name.length() > 0){
			return name;
		}
		return element.getType();
	}

	@Override
	public Image getImage(Object element) {
		return imageHelper.getImage("element.gif");
	}

}
