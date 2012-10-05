package org.rtext.editor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rtext.model.Element;
import org.rtext.model.RootElement;

public class RTextContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof RootElement) {
			return ((RootElement)parentElement).getChildren().toArray();
		}
		if (parentElement instanceof Element) {
			return ((Element) parentElement).getChildren().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Element) {
			return ((Element) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Element) {
			return !((Element) element).getChildren().isEmpty();
		}
		return false;
	}

}
