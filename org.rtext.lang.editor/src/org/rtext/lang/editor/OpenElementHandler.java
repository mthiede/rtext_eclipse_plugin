/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rtext.lang.commands.Elements.Element;


public class OpenElementHandler extends AbstractHandler {
	RTextEditor editor;
	
	public OpenElementHandler(RTextEditor editor) {
		this.editor = editor;
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OpenElementDialog dialog = OpenElementDialog.create(editor);
		dialog.open();
		Object[] result = dialog.getResult();
		if (result != null && result.length > 0) {
			Element pos = (Element)result[0];
			OpenEditorHelper.openInRTextEditor(editor.getSite().getPage(), pos.getFile(), pos.getLine());
		}
		return null;
	}

}
