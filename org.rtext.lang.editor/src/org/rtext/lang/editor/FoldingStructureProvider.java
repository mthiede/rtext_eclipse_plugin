/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.rtext.lang.model.RTextResource;

public class FoldingStructureProvider implements ModelChangeListener {

	private static final Annotation[] EMPTY_MODIFICATIONS = new Annotation[0];
	private RTextEditor editor;
	private ProjectionViewer viewer;
	private ProjectionChangeListener projectionListener;
	private FoldingRegionProvider foldingRegionProvider;
	
	public FoldingStructureProvider(){
		this(new FoldingRegionProvider());
	}

	public FoldingStructureProvider(FoldingRegionProvider foldingRegionProvider) {
		this.foldingRegionProvider = foldingRegionProvider;
	}

	public void install(RTextEditor editor, ProjectionViewer viewer) {
		Assert.isNotNull(editor);
		Assert.isNotNull(viewer);
		uninstall();
		this.editor = editor;
		this.viewer = viewer;
		projectionListener = new ProjectionChangeListener(viewer);
	}

	public void initialize() {
		calculateProjectionAnnotationModel(false);
	}

	public void uninstall() {
		if (isInstalled()) {
			handleProjectionDisabled();
			projectionListener.dispose();
			projectionListener = null;
			editor = null;
		}
	}

	protected final boolean isInstalled() {
		return editor != null;
	}

	public void handleModelChange(RTextResource root) {
		calculateProjectionAnnotationModel(false);
	}

	protected void handleProjectionEnabled() {
		handleProjectionDisabled();
		if (isInstalled()) {
			initialize();
			editor.getDocument().addModelListener(this);
		}
	}

	protected void handleProjectionDisabled() {
		if (editor.getDocument() != null) {
			editor.getDocument().removeModelListener(this);
		}
	}

	protected void calculateProjectionAnnotationModel(final boolean allowCollapse) {
		ProjectionAnnotationModel projectionAnnotationModel = viewer.getProjectionAnnotationModel();
		if (projectionAnnotationModel != null) {
			// make a defensive copy as we modify the folded positions in subsequent operations
			Set<Position> foldedPositions = new LinkedHashSet<Position>(foldingRegionProvider.getFoldingRegions(editor.getDocument()));
			Annotation[] newRegions = mergeFoldingRegions(foldedPositions, projectionAnnotationModel);
			updateFoldingRegions(allowCollapse, projectionAnnotationModel, foldedPositions, newRegions);
		}
		
	}

	@SuppressWarnings("unchecked")
	protected Annotation[] mergeFoldingRegions(Set<Position> foldedPositions,
			ProjectionAnnotationModel projectionAnnotationModel) {
		List<Annotation> deletions = new ArrayList<Annotation>();
		for (Iterator<Annotation> iterator = projectionAnnotationModel.getAnnotationIterator(); iterator.hasNext();) {
			Annotation annotation = iterator.next();
			if (annotation instanceof ProjectionAnnotation) {
				Position position = projectionAnnotationModel.getPosition(annotation);
				if (!foldedPositions.remove(position)) {
					deletions.add(annotation);
				}
			}
		}
		return deletions.toArray(new Annotation[deletions.size()]);
	}

	protected void updateFoldingRegions(boolean allowCollapse, ProjectionAnnotationModel model,
			Collection<Position> foldedPositions, Annotation[] deletions) {
		Map<ProjectionAnnotation, Position> additionsMap = new HashMap<ProjectionAnnotation, Position>(foldedPositions.size());
		for (Position foldedPosition: foldedPositions) {
			addProjectionAnnotation(allowCollapse, foldedPosition, additionsMap);
		}
		if (deletions.length != 0 || additionsMap.size() != 0) {
			model.modifyAnnotations(deletions, additionsMap, EMPTY_MODIFICATIONS);
		}
	}

	protected void addProjectionAnnotation(boolean allowCollapse, Position foldingRegion,
			Map<ProjectionAnnotation, Position> additionsMap) {
		ProjectionAnnotation projectionAnnotation = createProjectionAnnotation(allowCollapse);
		additionsMap.put(projectionAnnotation, foldingRegion);
	}

	protected ProjectionAnnotation createProjectionAnnotation(boolean isCollapsed) {
		return new ProjectionAnnotation(isCollapsed);
	}

	public class ProjectionChangeListener implements IProjectionListener {
		private ProjectionViewer projectionViewer;

		public ProjectionChangeListener(ProjectionViewer viewer) {
			Assert.isLegal(viewer != null);
			projectionViewer = viewer;
			projectionViewer.addProjectionListener(this);
		}

		public void dispose() {
			if (projectionViewer != null) {
				projectionViewer.removeProjectionListener(this);
				projectionViewer = null;
			}
		}

		public void projectionEnabled() {
			handleProjectionEnabled();
		}

		public void projectionDisabled() {
			handleProjectionDisabled();
		}
	}
}