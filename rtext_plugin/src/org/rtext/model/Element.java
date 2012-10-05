package org.rtext.model;

import java.util.List;

public class Element {

	private final String name;
	private final Element parent;
	private final int offset;
	private final int length;
	private final List<Element> children;
	private String type;
	
	public Element(String type, String name, Element parent, int offset, int length, List<Element> children) {
		this.type = type;
		this.name = name;
		this.parent = parent;
		this.offset = offset;
		this.length = length;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public Element getParent() {
		return parent;
	}

	public List<Element> getChildren() {
		return children;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
	
	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		result = prime * result + length;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + offset;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (length != other.length)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (offset != other.offset)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String parentString = "";
		if(parent != null){
			parentString = parent.name + "/";
		}
		String result = type + " " + name + "(" + parentString + offset + "/" + length + ")";
		
		if(!children.isEmpty()){
			result += " {\n";
			for (Element child : children) {
				result += child + "\n";
			}
			result += "}";
		}
		return result;
	}

	

}
