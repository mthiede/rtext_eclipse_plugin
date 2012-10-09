package org.rtext.editor;

import org.rtext.model.RTextResource;

public interface ModelChangeListener {

	void handleModelChange(RTextResource root);

}
