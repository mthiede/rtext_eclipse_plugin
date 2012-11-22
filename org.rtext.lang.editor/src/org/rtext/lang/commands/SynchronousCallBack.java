/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import static org.rtext.lang.util.Wait.waitUntil;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.util.Condition;

public class SynchronousCallBack<T extends Response> implements Callback<T> {

	private T response;

	public void handleResponse(T response) {
		this.response = response;
	}

	public void handleProgress(Progress progress) {
	}

	public void waitForResponse() throws TimeoutException {
		waitUntil(new Condition() {
			public boolean applies() {
				return response != null;
			}
		});
	}

	public T response() {
		return response;
	}

	public void handleError(String error) {
	}

	public void commandSent() {
	}
}