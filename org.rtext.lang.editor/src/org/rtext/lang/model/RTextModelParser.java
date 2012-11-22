/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.model;

import static org.rtext.lang.model.ElementBuilder.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RTextModelParser extends AbstractRTextParser<Element> {

	private static Element EOF = element().build();
	private Stack<ElementBuilder> parents;
	private ElementBuilder current;
	private List<ElementBuilder> roots = new ArrayList<ElementBuilder>();

	public List<Element> parse(){
		init();
		Element token = nextToken();
		while(token != EOF){
			token = nextToken();
		}
		if(current == null){
			current = element();
		}
		List<Element> result = new ArrayList<Element>(roots.size());
		for (ElementBuilder builder : roots) {
			result.add(builder.build());
		}
		return result;
	}

	private void init() {
		parents = new Stack<ElementBuilder>();
		roots.clear();
		current = null;
	}

	@Override
	protected Element createEndOfFile() {
		return EOF;
	}

	@Override
	protected Element createComment() {
		return null;
	}

	@Override
	protected Element createLabel() {
		increaseLength();
		return null;
	}

	@Override
	protected Element createReference() {
		increaseLength();
		return null;
	}

	@Override
	protected Element createCommand() {
		add(element()
			.offset(getTokenOffset())
			.type(currentText()));
		increaseLength();
		return null;
	}

	private void add(ElementBuilder newElement) {
		if(!parents.isEmpty()){
			parents.peek().addChild(newElement);
		}else{
			roots.add(newElement);
		}
		current = newElement;
	}

	private void increaseLength() {
		current.length(currentOffset() - current.getOffset());
	}

	private String currentText() {
		return get(getTokenOffset(), getTokenLength()).trim();
	}

	@Override
	protected Element createIdentifier() {
		current.name(currentText());
		increaseLength();
		return null;
	}

	@Override
	protected Element createNumber() {
		increaseLength();
		return null;
	}

	@Override
	protected Element createString() {
		increaseLength();
		return null;
	}

	@Override
	protected Element createDefault() {
		return null;
	}
	
	@Override
	protected void openBlock() {
		parents.push(current);
	}
	
	@Override
	protected void closeBlock() {
		if(parents.isEmpty()){
			return; // syntax error
		}
		ElementBuilder parent = parents.peek();
		int length = currentOffset() - parent.getOffset() + 1;
		parent.length(length);
		parents.pop();
	}
}
