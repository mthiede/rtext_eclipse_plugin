package org.rtext.lang.util;

public class Exceptions {
	public static void rethrow(Throwable e){
		throw new RuntimeException(e);
	}
}
