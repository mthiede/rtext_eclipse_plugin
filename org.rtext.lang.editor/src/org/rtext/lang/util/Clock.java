package org.rtext.lang.util;

public interface Clock {
	
	public static final Clock SYSTEM_CLOCK = new Clock() {
		public long currentTime() {
			return System.currentTimeMillis();
		}
	};
	
	long currentTime();
}
