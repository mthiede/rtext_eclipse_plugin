package org.rtext.editor;

import org.eclipse.jface.viewers.LabelProvider;
import org.rtext.model.Element;

public class RTextLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Element) {
			return ((Element) element).getName();
		}
		return super.getText(element);
	}


}
