package org.rtext.lang.backend2;

import static org.rtext.lang.util.Wait.waitUntil;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.util.Condition;

class SynchronousCallBack<T extends Response> implements Callback<T> {

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