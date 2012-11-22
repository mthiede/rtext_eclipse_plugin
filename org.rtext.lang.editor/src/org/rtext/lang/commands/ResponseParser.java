/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

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
