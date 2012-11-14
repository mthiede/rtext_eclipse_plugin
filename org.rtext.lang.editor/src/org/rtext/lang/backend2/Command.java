package org.rtext.lang.backend2;

import com.google.gson.annotations.SerializedName;

public class Command {
	
	private static int COMMAND_COUNTER = 0;
	private final String type;
	private final String command;
	@SerializedName("invocation_id") private final int invocationId;
	
	public Command(int invocationId, String type, String command) {
		this.type = type;
		this.command = command;
		this.invocationId = invocationId;
	}

	public Command(String command) {
		this(newInvocationId(), "request", command);
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

	@Override
	public String toString() {
		return "Command [invocationId=" + invocationId + ", type=" + type
				+ ", command=" + command + "]";
	}
	
	
	
}
