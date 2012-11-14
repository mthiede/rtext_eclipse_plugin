package org.rtext.lang.backend2;

import java.util.List;

public class Proposals extends Response {

	private List<Option> options;

	public static class Option{
		private String insert;
		private String display;
		
		public Option(String insert, String display) {
			super();
			this.insert = insert;
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}
		
		public String getInsert() {
			return insert;
		}

		@Override
		public String toString() {
			return insert + " -> " + display;
		}
		
	}
	
	public Proposals(int invocationId, String type, List<Option> options) {
		super(invocationId, type);
		this.options = options;
	}
	
	public List<Option> getOptions() {
		return options;
	}
}
