package org.rtext.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementBuilder {

	private String name = "";
	private String type = "";
	private Element parent = null;
	private int offset = 0;
	private int length = 0;
	private List<ElementBuilder> children = emptyList();
	
	public ElementBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public ElementBuilder type(String type) {
		this.type = type;
		return this;
	}
	
	public ElementBuilder length(int length) {
		this.length = length;
		return this;
	}
	
	public ElementBuilder offset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public ElementBuilder parent(Element parent) {
		this.parent = parent;
		return this;
	}
	
	public ElementBuilder children(ElementBuilder... children){
		getChildren().addAll(asList(children));
		return this;
	}

	private List<ElementBuilder> getChildren() {
		if(children == Collections.<ElementBuilder>emptyList()){
			children = new ArrayList<ElementBuilder>();
		}
		return this.children;
	}
	
	public Element build(){
		List<Element> newChildren;
		if(children.isEmpty()){
			newChildren = emptyList();
		}else{
			newChildren = new ArrayList<Element>(children.size());
		}
		Element result = new Element(type, name, parent, offset, length, newChildren);
		for (ElementBuilder childBuilder : children) {
			newChildren.add(childBuilder.parent(result).build());
		}
		return result;
	}

	public static ElementBuilder element() {
		return new ElementBuilder();
	}
	
	public static ElementBuilder element(String type) {
		return new ElementBuilder().type(type);
	}

	public int getLength() {
		return length;
	}

	public ElementBuilder addChild(ElementBuilder builder) {
		getChildren().add(builder);
		return this;
	}
}
