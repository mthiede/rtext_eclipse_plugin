package org.rtext.lang.util;

public class Exceptions {
	public static void rethrow(Exception e){
		throw new RuntimeException(e);
	}
}
