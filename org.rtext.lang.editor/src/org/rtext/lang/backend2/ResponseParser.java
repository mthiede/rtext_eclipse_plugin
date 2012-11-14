package org.rtext.lang.backend2;

import com.google.gson.Gson;

public class ResponseParser {
	
	Gson jsonParser = new Gson();

	public boolean parse(String message, Callback callback) {
		System.out.println(message);
		if(message.startsWith("{\"type\":\"progress\"")){
			callback.handleProgress(parse(message, Progress.class));
			return false;
		}else{
			callback.handleResponse(parse(message, Response.class));
			return true;
		}
	}

	protected <T> T parse(String message, Class<T> responseType) {
		return jsonParser.fromJson(message, responseType);
	}
	
}
