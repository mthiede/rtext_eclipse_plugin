/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.rtext.lang.model.Element;

public class RTextLabelProvider extends LabelProvider {
	
	private ImageHelper imageHelper;
	
	public RTextLabelProvider(){
		this(new PluginImageHelper());
	}

	public RTextLabelProvider(ImageHelper imageHelper) {
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
