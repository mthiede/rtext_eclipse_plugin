/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.specs.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.rtext.lang.model.Element;

import com.google.common.base.Objects;

@SuppressWarnings("restriction")
public class IsElement extends TypeSafeMatcher<Element> {

	private String name;
	private String type;
	private List<IsElement> childMatchers;

	
	public static IsElement element(String type){
		return new IsElement(type, "", Collections.<IsElement>emptyList());
	}
	
	public static IsElement element(String type, String name){
		return new IsElement(type, name, Collections.<IsElement>emptyList());
	}
	
	public static IsElement element(String type, String name, IsElement... childMatchers){
		return new IsElement(type, name, Arrays.asList(childMatchers));
	}
	
	public IsElement(String type, String name, List<IsElement> childMatchers) {
		this.name = name;
		this.type = type;
		this.childMatchers = childMatchers;
	}

	public void describeTo(Description description) {
		description.appendText(type + " " + name);
		if(childMatchers.size() > 0){
			description.appendValueList("{\n", "\n", "\n}", childMatchers);
		}
	}

	@Override
	public boolean matchesSafely(Element item) {
		boolean isElement = Objects.equal(type, item.getType()) && Objects.equal(name, item.getName());
		if(!isElement){
			return false;
		}
		if(item.getChildren().size() != item.getChildren().size()){
			return false;
		}
		for(int i = 0; i < item.getChildren().size(); i++){
			if(!childMatchers.get(i).matches(item.getChildren().get(i))){
				return false;
			}
		}
		return true;
	}

}
