package org.rtext.model;

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
