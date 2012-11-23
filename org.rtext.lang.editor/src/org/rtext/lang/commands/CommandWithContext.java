package org.rtext.lang.commands;

import java.util.List;

import org.rtext.lang.backend.DocumentContext;

public class CommandWithContext<T extends Response> extends Command<T> {

	protected List<String> context;
	protected int column;

	public CommandWithContext(String string, Class<T> responseType, DocumentContext documentContext) {
		super(string, responseType);
		this.context = documentContext.getParseContext();
		this.column = documentContext.getColumn();
	}

	public List<String> getContext() {
		return context;
	}

	public int getColumn() {
		return column;
	}

}