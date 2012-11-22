/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend2;


@SuppressWarnings("serial")
public class BackendException extends RuntimeException {
	public BackendException(String message) {
		super(message);
	}

	public BackendException(String string, Exception cause) {
		super(string, cause);
	}
}
