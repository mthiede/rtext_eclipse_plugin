package org.rtext.lang.backend2;

import com.google.gson.Gson;

public class ResponseParser {
	
	Gson jsonParser = new Gson();

	public <T extends Response> boolean parse(String message, Callback<T> callback, Class<T> responseType) {
		if(message.startsWith("{\"type\":\"progress\"")){
			callback.handleProgress(parse(message, Progress.class));
			return false;
		}else{
			callback.handleResponse(parse(message, responseType));
			return true;
		}
	}

	protected <T> T parse(String message, Class<T> responseType) {
		return jsonParser.fromJson(message, responseType);
	}
	
}
