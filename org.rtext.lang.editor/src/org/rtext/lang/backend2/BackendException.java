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
