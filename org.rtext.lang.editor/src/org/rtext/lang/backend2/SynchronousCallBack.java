package org.rtext.lang.backend2;

import static org.rtext.lang.util.Wait.waitUntil;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.util.Condition;
import org.rtext.lang.util.Exceptions;

class SynchronousCallBack implements Callback {

	private Response response;

	public void handleResponse(Response response) {
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

	public Response response() {
		return response;
	}

	public void handleError(String error) {
	}
}