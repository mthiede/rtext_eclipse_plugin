package org.rtext.editor;

import org.rtext.model.RTextResource;

public interface IRTextDocument {

	public abstract <T> T readOnly(IUnitOfWork<T, RTextResource> work);

	public abstract <T> T modify(IUnitOfWork<T, RTextResource> work);

	public abstract void removeModelListener(ModelChangeListener listener);

	public abstract void addModelListener(ModelChangeListener listener);

}