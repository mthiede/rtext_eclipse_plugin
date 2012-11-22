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

	private String message = "Timeout occurred";
	private long duration = 5000l;
	private long pollEvery = 50l;

	public static void waitUntil(Condition condition) throws TimeoutException{
		Wait wait = new Wait(Sleeper.SYSTEM_SLEEPER, Clock.SYSTEM_CLOCK);
		wait.until(condition);
	}

	private final Sleeper sleeper;
	private final Clock clock;
	
	public Wait(Sleeper sleeper, Clock clock){
		this.sleeper = sleeper;
		this.clock = clock;
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