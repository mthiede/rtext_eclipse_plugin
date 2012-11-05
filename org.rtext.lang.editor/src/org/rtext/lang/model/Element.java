/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.model;

import java.util.List;

import org.eclipse.jface.text.Position;

public class Element {

	private final String name;
	private final Element parent;
	private final List<Element> children;
	private final String type;
	private Position position;
	
	public Element(String type, String name, Element parent, Position position, List<Element> children) {
		this.type = type;
		this.name = name;
		this.parent = parent;
		this.position = position;
		this.children = children;
//		assertInRange(parent);
	}

//	private void assertInRange(Element container) {
//		while(container != null){
//			Position containerPosition = container.getPosition();
//			if(containerPosition.getOffset() >= position.getOffset()){
//				throw new IllegalArgumentException(this + ": offset out of range: " + position.offset);
//			}
//			int containerEndOffset = containerPosition.offset + containerPosition.length;
//			if(containerEndOffset < position.offset + position.length){
//				throw new IllegalArgumentException(this + ": length out of range: " + position.length);
//			}
//			container = container.getParent();
//		}
//	}

	public String getName() {
		return name;
	}

	public Element getParent() {
		return parent;
	}

	public List<Element> getChildren() {
		return children;
	}

	public Position getPosition() {
		return position;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		String parentString = "";
		if(parent != null){
			parentString = parent.name + "/";
		}
		String result = type + " " + name + "(" + parentString + position.getOffset() + "/" + position.getLength() + ")";
		
		if(!children.isEmpty()){
			result += " {\n";
			for (Element child : children) {
				result += child + "\n";
			}
			result += "}";
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
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
		Element other = (Element) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	
}
