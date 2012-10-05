package org.rtext.util;

public class Expectations {
	
	public static void expectType(Class<?> expectedType, Object candidate){
		if(expectedType.isInstance(candidate)){
			return;
		}
		throw new IllegalArgumentException("Unexpected type. Expected '" + expectedType.getName() + " but was " + candidate.getClass().getName());
	}

}
