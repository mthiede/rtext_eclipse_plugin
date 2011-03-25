package org.rtext.backend;

public class Command {
	private String name;
	private String data;
	
	public Command(String name, String data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return data;
	}
}
