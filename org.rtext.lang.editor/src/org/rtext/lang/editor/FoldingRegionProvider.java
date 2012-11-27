/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Position;
import org.rtext.lang.document.IRTextDocument;
import org.rtext.lang.model.Element;
import org.rtext.lang.model.RTextResource;
import org.rtext.lang.util.IUnitOfWork;

public class FoldingRegionProvider {

	private static final int MAX_SIZE = 10000;

	public Collection<? extends Position> getFoldingRegions(
			IRTextDocument document) {
		if(document.getNumberOfLines() > MAX_SIZE){
			return Collections.emptyList();
		}
		return document.readOnly(new IUnitOfWork<Collection<? extends Position>, RTextResource>(){
			public Collection<? extends Position> exec(RTextResource state)
					throws Exception {
				return calculateRegions(state);
			}
		});
	}
	
	private Collection<? extends Position> calculateRegions(
			RTextResource state) {
		List<Position> foldedPositions = new LinkedList<Position>();
		addChildPositions(foldedPositions, state.getChildren());
		return foldedPositions;
	}

	private void addChildPositions(List<Position> foldedPositions,
			List<Element> children) {
		for (Element element : children) {
			addPosition(foldedPositions, element);
		}
	}

	private void addPosition(List<Position> foldedPositions,
			Element element) {
		if(!element.getChildren().isEmpty()){
			foldedPositions.add(element.getPosition());
		}
		addChildPositions(foldedPositions, element.getChildren());
	}
}
