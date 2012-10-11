package org.rtext.editor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Position;
import org.rtext.model.Element;
import org.rtext.model.RTextResource;

public class FoldingRegionProvider {

	public Collection<? extends Position> getFoldingRegions(
			IRTextDocument document) {
		return document.readOnly(new IUnitOfWork<Collection<? extends Position>, RTextResource>(){
			@Override
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
