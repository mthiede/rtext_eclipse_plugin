package org.rtext.model;

import static java.util.Collections.singletonList;
import static org.rtext.model.ElementBuilder.element;

import java.util.List;
import java.util.Stack;

public class RTextModelParser extends AbstractRTextParser<Element> {

	private static Element EOF = element().build();
	private Stack<ElementBuilder> parents;
	private ElementBuilder current;
	private ElementBuilder root;
	
	private int start = 0;
	
	
	public List<Element> parse(){
		init();
		while(nextToken() != EOF){
			nextToken();
		}
		Element result = root.build();
		return singletonList(result);
	}

	private void init() {
		parents = new Stack<ElementBuilder>();
		root = null;
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
		start = getTokenOffset();
		increaseLength();
		return null;
	}

	private void add(ElementBuilder newElement) {
		if(!parents.isEmpty()){
			parents.peek().addChild(newElement);
		}
		if(root == null){
			root = newElement;
		}
		current = newElement;
	}

	private void increaseLength() {
		int length = getTokenOffset() + getTokenLength() - start;
		current.length(length);
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
		increaseLength();
		return null;
	}
	
	@Override
	protected void openBlock() {
		parents.push(current);
	}
	
	@Override
	protected void closeBlock() {
		parents.pop();
	}
}
