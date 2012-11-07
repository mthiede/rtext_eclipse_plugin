package org.rtext.lang.backend2;

public interface Callback {
	void handleProgress(Progress progress);
	void handleResponse(Response response);
	void handleError(String error);
}
