package org.rtext.lang.backend;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.rtext.lang.util.Strings;

public class DocumentContext {

	public static final DocumentContext INVALID = DocumentContext.create(0);
	private List<String> parseContext;
	private int column;
	private Map<Integer, Integer> lineBreaks;

	public static DocumentContext create(int column, String... parseContext) {
		return new DocumentContext(Arrays.asList(parseContext), column, new HashMap<Integer, Integer>());
	}

	public DocumentContext(List<String> parseContext, int column, Map<Integer, Integer> lineBreaks) {
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

	public Map<Integer, Integer> getLineBreaks() {
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
