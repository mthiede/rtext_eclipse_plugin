package org.rtext.lang.backend2;

import com.google.gson.annotations.SerializedName;

public class Command<T extends Response> {
	
	private static int COMMAND_COUNTER = 0;
	private final String type;
	private final String command;
	@SerializedName("invocation_id") private final int invocationId;
	private transient Class<T> responseType;
	
	public Command(int invocationId, String type, String command, Class<T> responseType) {
		this.type = type;
		this.command = command;
		this.invocationId = invocationId;
		this.responseType = responseType;
	}

	public Command(String command, Class<T> responseType) {
		this(newInvocationId(), "request", command, responseType);
    }
    
	private static int newInvocationId() {
		COMMAND_COUNTER++;
		return COMMAND_COUNTER;
	}

	public String getCommand() {
		return command;
	}
	
	public int getInvocationId() {
		return invocationId;
	}
	
	public String getType() {
		return type;
	}
	
	public Class<T> getResponseType(){
		return responseType;
	}

	@Override
	public String toString() {
		return "Command [invocationId=" + invocationId + ", type=" + type
				+ ", command=" + command + "]";
	}
	
}
