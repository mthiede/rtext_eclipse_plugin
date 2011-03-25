package org.rtext.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rtext.editor.OpenElementDialog.ElementDescriptor;


public class OpenElementHandler extends AbstractHandler {
	Editor editor;
	
	public OpenElementHandler(Editor editor) {
		this.editor = editor;
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		OpenElementDialog dialog = new OpenElementDialog(editor);
		dialog.open();
		Object[] result = dialog.getResult();
		if (result != null && result.length > 0) {
			ElementDescriptor pos = (ElementDescriptor)result[0];
			OpenEditorHelper.openInRTextEditor(editor.getSite().getPage(), pos.getFilename(), pos.getLine());
		}
		return null;
	}

}
