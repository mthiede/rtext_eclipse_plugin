package org.rtext.lang.backend2;

import java.lang.reflect.Method;

import com.google.gson.annotations.SerializedName;

public class Command<T extends Response> {
	
	private static int COMMAND_COUNTER = 0;
	private final String type;
	private final String command;
	@SerializedName("invocation_id") private final int invocationId;
	private transient Class<T> responseType;
	
	@SuppressWarnings("unchecked")
	public Command(int invocationId, String type, String command) {
		this.type = type;
		this.command = command;
		this.invocationId = invocationId;
		responseType = (Class<T>) findExpectedType(getClass());
	}

	public Command(String command) {
		this(newInvocationId(), "request", command);
    }
    
    private static Class<?> findExpectedType(Class<?> fromClass) {
        for (Class<?> c = fromClass; c != Object.class; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (isGetResponseTypeMethod(method)) {
                    return method.getParameterTypes()[0];
                }
            }
        }
        throw new Error("Cannot determine correct type for getResponseType() method.");
    }
    
    @SuppressWarnings("unused")
	protected void setReturnType(T type){
    	
    }
    
    private static boolean isGetResponseTypeMethod(Method method) {
        return method.getName().equals("setReturnType") 
            && method.getParameterTypes().length == 1
            && !method.isSynthetic(); 
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
