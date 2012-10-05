package org.rtext.model;

import java.util.ArrayList;
import java.util.List;

public class RootElement extends Element {

	private List<Element> children = new ArrayList<Element>();
	
	public RootElement() {
		super("Resource");
	}

	public void update(Element child) {
		children.clear();
		children.add(child);
	}
	
	@Override
	public List<Element> getChildren() {
		return children;
	}

}
