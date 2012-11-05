package org.rtext.lang.backend2;

import java.util.List;

public class Response {
	
	private final String type;
	private final List<String> problems;
	
	public Response(String type, List<String> problems) {
		this.type = type;
		this.problems = problems;
	}
	
	
	public String getType() {
		return type;
	}
	
	public List<String> getProblems() {
		return problems;
	}
}
