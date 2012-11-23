package org.rtext.lang.backend;

import java.util.List;

public class DocumentContext {

	private List<String> parseContext;
	private int column;

	public DocumentContext(List<String> parseContext, int column) {
		this.parseContext = parseContext;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}
	
	public List<String> getParseContext() {
		return parseContext;
	}

}
