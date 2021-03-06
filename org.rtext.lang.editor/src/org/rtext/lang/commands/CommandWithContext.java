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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + column;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandWithContext<?> other = (CommandWithContext<?>) obj;
		if (column != other.column)
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		return true;
	}

}