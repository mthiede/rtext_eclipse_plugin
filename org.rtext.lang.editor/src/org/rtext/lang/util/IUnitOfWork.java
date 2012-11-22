/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;


public interface IUnitOfWork<R,P> {

	R exec(P state) throws Exception;
	
	public static abstract class Void<T> implements IUnitOfWork<Object,T> {
		public final Object exec(T state) throws Exception {
			process(state);
			return null;
		}

		public abstract void process(T state) throws Exception;
	}
	
}

