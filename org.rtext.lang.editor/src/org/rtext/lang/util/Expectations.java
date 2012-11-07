/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

import java.io.File;

public class Expectations {
	
	public static void expectType(Class<?> expectedType, Object candidate){
		if(expectedType.isInstance(candidate)){
			return;
		}
		throw new IllegalArgumentException("Unexpected type. Expected '" + expectedType.getName() + " but was " + candidate.getClass().getName());
	}

	public static void expectNotNull(Object obj) {
		if(obj != null){
			return;
		}
		throw new IllegalArgumentException("should not be null");
	}

	public static void expectNotEmpty(String string) {
		expectNotNull(string);
		if(string.length() > 0){
			return;
		}
		throw new IllegalArgumentException("string should not be empty");
	}

	public static void expectExists(File file) {
		expectNotNull(file);
		if(file.exists()){
			return;
		}
		throw new IllegalArgumentException("file '" + file + "' does not exist");
	}

	public static void greaterThanZero(int number) {
		if(number <= 0){
			throw new IllegalArgumentException("should be greater than zero, but was: " + number);
		}
	}

}
