/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.document;

import org.rtext.lang.model.ModelChangeListener;
import org.rtext.lang.model.RTextResource;
import org.rtext.lang.util.IUnitOfWork;

public interface IRTextDocument {

	public abstract <T> T readOnly(IUnitOfWork<T, RTextResource> work);

	public abstract <T> T modify(IUnitOfWork<T, RTextResource> work);

	public abstract void removeModelListener(ModelChangeListener listener);

	public abstract void addModelListener(ModelChangeListener listener);
	
	public abstract int getNumberOfLines();

}