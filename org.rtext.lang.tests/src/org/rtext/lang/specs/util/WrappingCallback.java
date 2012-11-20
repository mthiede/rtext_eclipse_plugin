package org.rtext.lang.specs.util;

import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.rtext.lang.backend2.Callback;
import org.rtext.lang.backend2.Progress;
import org.rtext.lang.backend2.Response;

public class WrappingCallback<T extends Response> implements Callback<T>{
	
	private Callback<T> toWrap;
	private T response;


	public static <T extends Response> Callback<T> create(Callback<T> toWrap){
		return new WrappingCallback<T>(toWrap);
	}

	public WrappingCallback(Callback<T> toWrap) {
		this.toWrap = toWrap;
	}

	public void commandSent() {
		toWrap.commandSent();
	}

	public void handleProgress(Progress progress) {
		toWrap.handleProgress(progress);
	}

	public void handleResponse(T response) {
		toWrap.handleResponse(response);
		this.response = response;
	}

	public void handleError(String error) {
		toWrap.handleError(error);
	}
	
	public void waitForResponse(){
		Wait.waitUntil(new Function1<WaitConfig, Boolean>() {
			
			public Boolean apply(WaitConfig p) {
				return response != null;
			}
		});
	}
}
