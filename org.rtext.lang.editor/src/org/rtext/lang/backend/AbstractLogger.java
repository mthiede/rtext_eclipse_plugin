package org.rtext.lang.backend;

public class AbstractLogger {

	protected void info(String message) {
		print("INFO", message);
	}

	protected void error(Throwable t) {
		print("ERROR", t.getMessage());
		t.printStackTrace();
	}

	private void print(String category, String message) {
		System.out.println("[" + category + "] [Thread " + Thread.currentThread().getId() + "] "  + " " + getClass().getSimpleName() + ": " + message);
	}

}