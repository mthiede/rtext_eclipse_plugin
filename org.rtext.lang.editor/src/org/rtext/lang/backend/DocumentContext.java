package org.rtext.lang.backend;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.rtext.lang.util.Strings;

public class DocumentContext {

	public static final DocumentContext INVALID = DocumentContext.create(0);
	private List<String> parseContext;
	private int column;
	private Set<Integer> lineBreaks;

	public static DocumentContext create(int column, String... parseContext) {
		return new DocumentContext(Arrays.asList(parseContext), column, new HashSet<Integer>());
	}

	public DocumentContext(List<String> parseContext, int column, Set<Integer> lineBreaks) {
		this.parseContext = parseContext;
		this.column = column;
		this.lineBreaks = lineBreaks;
	}

	public int getColumn() {
		return column;
	}

	public List<String> getParseContext() {
		return parseContext;
	}

	public Set<Integer> getLineBreaks() {
		return lineBreaks;
	}

	@Override
	public String toString() {
		return column + ": ['" + Strings.join(parseContext, "', '") + "']";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result
				+ ((parseContext == null) ? 0 : parseContext.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentContext other = (DocumentContext) obj;
		if (column != other.column)
			return false;
		if (parseContext == null) {
			if (other.parseContext != null)
				return false;
		} else if (!parseContext.equals(other.parseContext))
			return false;
		return true;
	}

}
