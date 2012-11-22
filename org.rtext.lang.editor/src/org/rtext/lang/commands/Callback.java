/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

public interface Callback<T extends Response> {
	void commandSent();
	void handleProgress(Progress progress);
	void handleResponse(T response);
	void handleError(String error);
}
