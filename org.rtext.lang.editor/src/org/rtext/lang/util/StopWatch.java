/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

public class StopWatch {

	private long start = System.currentTimeMillis();

	public long stop(String label) {
		long now = System.currentTimeMillis();
		try {
			long elapsedTime = now - start;
			System.out.println(label + ": " + elapsedTime + "ms");
			return elapsedTime;
		} finally {
			start = now;
		}
	}

}
