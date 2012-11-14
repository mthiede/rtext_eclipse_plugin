package org.rtext.lang.backend2;

import java.util.List;

public class Proposals extends Response {

	private List<Option> options;

	public static class Option{
		String insert;
		String display;
	}
	
	public Proposals(int invocationId, String type, List<Option> options) {
		super(invocationId, type);
		this.options = options;
	}
	
	public List<Option> getOptions() {
		return options;
	}
}
