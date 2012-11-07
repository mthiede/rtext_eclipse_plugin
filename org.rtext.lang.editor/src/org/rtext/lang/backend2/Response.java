package org.rtext.lang.backend2;

import com.google.gson.annotations.SerializedName;

public class Response {
	
	private final String type;

	@SerializedName("invocation_id") private int invocationId;
	
	public Response(int invocationId, String type) {
		this.invocationId = invocationId;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public int getInvocationId() {
		return invocationId;
	}
	
}
