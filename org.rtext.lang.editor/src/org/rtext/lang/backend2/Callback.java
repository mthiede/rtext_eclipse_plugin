package org.rtext.lang.backend2;

public interface Callback<T extends Response> {
	void handleProgress(Progress progress);
	void handleResponse(T response);
	void handleError(String error);
}
