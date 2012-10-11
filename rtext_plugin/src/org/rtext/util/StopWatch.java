package org.rtext.util;

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
