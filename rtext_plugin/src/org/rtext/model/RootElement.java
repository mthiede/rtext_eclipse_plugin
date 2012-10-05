package org.rtext.model;

import java.util.ArrayList;
import java.util.List;

public class RootElement  {

	private List<Element> children = new ArrayList<Element>();

	public void update(List<Element> children) {
		this.children = children;
	}
	
	public List<Element> getChildren() {
		return children;
	}
 
}
