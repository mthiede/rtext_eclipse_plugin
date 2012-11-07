package org.rtext.lang.backend2;

public class Progress extends Response{
	
	private int percentage;
	
	public Progress(int invocationId, String type, int percentage) {
		super(invocationId, type);
		this.percentage = percentage;
	}
	
	public int getPercentage() {
		return percentage;
	}
}
