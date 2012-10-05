package org.rtext.model;

import java.util.Collections;
import java.util.List;

public class Element {

	private final String name;
	private final Element parent;
	
	public Element(String name) {
		this(name, null);
	}

	public Element(String name, Element parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public Element getParent() {
		return parent;
	}

	public List<Element> getChildren() {
		return Collections.emptyList();
	}

	public int getOffset() {
		return 0;
	}

	public int getLength() {
		return 0;
	}

}
