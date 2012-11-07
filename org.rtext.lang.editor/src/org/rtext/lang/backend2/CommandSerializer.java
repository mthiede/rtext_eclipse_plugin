package org.rtext.lang.backend2;

import com.google.gson.Gson;

public class CommandSerializer {

	private Gson gson = new Gson();

	public String serialize(Command command){
		String jsonString = gson.toJson(command);
		return jsonString.length() + jsonString;
	}
	
}
