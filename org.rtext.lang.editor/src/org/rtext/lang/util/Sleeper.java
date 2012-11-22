/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

public interface Sleeper {
	
	public static final Sleeper SYSTEM_SLEEPER = new Sleeper(){
		public void sleep(long milis) throws InterruptedException {
			Thread.sleep(milis);
		}
	};
	
	public void sleep(long milis) throws InterruptedException;
}
