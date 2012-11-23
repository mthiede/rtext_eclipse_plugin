/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

import org.rtext.lang.backend.BackendException;

public class Exceptions {
	public static void rethrow(Throwable e){
		throw new BackendException(e);
	}
}
