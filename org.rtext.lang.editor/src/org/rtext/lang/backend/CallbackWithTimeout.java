package org.rtext.lang.backend;

import static org.rtext.lang.util.Wait.waitUntil;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Progress;
import org.rtext.lang.commands.Response;
import org.rtext.lang.util.Condition;

public class CallbackWithTimeout<T extends Response> implements Callback<T> {

	private T response;
	private final String message;
	private final long duration;
	private volatile boolean isWaiting = false;
	
	public static <T extends Response> CallbackWithTimeout<T> waitForResponse(String message, long duration){
		return new CallbackWithTimeout<T>(message, duration);
	}
	
	public CallbackWithTimeout(String message, long duration) {
		this.message = message;
		this.duration = duration;
	}

	public void commandSent() {
	}

	public void handleProgress(Progress progress) {
	}

	public void handleResponse(T response) {
		this.response = response;
	}

	public void handleError(String error) {
	}
	
	public T waitForResponse() throws TimeoutException{
		try {
			isWaiting = true;
			waitUntil(new Condition() {
				
				public boolean applies() {
					return response != null;
				}
			}, duration, message);
			return response;
		} catch (TimeoutException e) {
			throw new TimeoutException("Backend is busy");
		}finally{
			isWaiting = false;
		}
	}

	public boolean isWaiting() {
		return isWaiting;
	}
}
