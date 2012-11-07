package org.rtext.lang.specs.util;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.rtext.lang.backend2.Callback;
import org.rtext.lang.backend2.Progress;
import org.rtext.lang.backend2.Response;

public class TestCallBack implements Callback {
	
	private Response response;
	private List<Progress> progress = newArrayList();
	private String error;
	
	public void handleProgress(Progress progress) {
		this.progress.add(progress);
	}

	public void handleResponse(Response response) {
		this.response = response;
	}
	
	public Response getResponse() {
		return response;
	}
	
	public List<Progress> getProgress() {
		return progress;
	}

	public void handleError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

}