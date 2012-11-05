/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Position;

public class RTextResource extends Element{

	public RTextResource() {
		super("Document", "", null, new Position(0, 0), new ArrayList<Element>());
	}

	public void update(int length, List<Element> elements) {
		this.getPosition().setLength(length);
		getChildren().clear();
		getChildren().addAll(elements);
	}
 
}
