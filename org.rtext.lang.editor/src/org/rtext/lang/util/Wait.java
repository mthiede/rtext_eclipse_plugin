/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

import java.util.concurrent.TimeoutException;

public class Wait {

	private static final long DEFAULT_TIMEOUT = 5000l;
	private String message = "Timeout occurred";
	private final long duration;
	private final long pollEvery = 50l;
	
	public static void waitUntil(Condition condition, long duration, String message) throws TimeoutException{
		Wait wait = new Wait(Sleeper.SYSTEM_SLEEPER, Clock.SYSTEM_CLOCK, duration, message);
		wait.until(condition);
	}

	public static void waitUntil(Condition condition) throws TimeoutException{
		waitUntil(condition, DEFAULT_TIMEOUT, "Timeout occured");
	}

	private final Sleeper sleeper;
	private final Clock clock;
	
	public Wait(Sleeper sleeper, Clock clock, long duration, String message){
		this.sleeper = sleeper;
		this.clock = clock;
		this.message = message;
		this.duration = duration;
	}

	public void until(Condition condition) throws TimeoutException {
		long start = clock.currentTime();
		while (!condition.applies()) {
			if (timeOut(start)) {
				throw new TimeoutException(message);
			}
			try {
				sleeper.sleep(pollEvery);
			} catch (InterruptedException e) {}
		}
	}

	public boolean timeOut(long start){
		return clock.currentTime() > start + duration;
	}
}