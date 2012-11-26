/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import static org.eclipse.ui.texteditor.MarkerUtilities.setLineNumber;
import static org.eclipse.ui.texteditor.MarkerUtilities.setMessage;
import static org.rtext.lang.RTextPlugin.logError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.commands.LoadedModel.FileProblems;
import org.rtext.lang.commands.LoadedModel.Problem;
import org.rtext.lang.util.FileLocator;
import org.rtext.lang.util.Procedure;
import org.rtext.lang.util.RTextJob;

public class LoadModelCallback extends WorkspaceCallback<LoadedModel> {

	public static final String RTEXT_MARKERS = "org.rtext.lang.editor.makers";
	public static final String PROBLEM_MARKER_JOB = "Updating Problem markers";

	public static class ProblemUpdateJobFactory{
		public ProblemUpdateJob create(Map<IResource, List<Problem>> problems, ConnectorScope scope){
			return new ProblemUpdateJob(problems, scope);
		}
	}
	
	public static class ProblemUpdateJob extends RTextJob{
		private Map<IResource, List<Problem>> problems;
		private ConnectorScope scope;

		public ProblemUpdateJob(Map<IResource, List<Problem>> problems, ConnectorScope scope) {
			super(PROBLEM_MARKER_JOB);
			this.problems = problems;
			this.scope = scope;
			setRule(lockAll(problems.keySet()));
		}
		
		public MultiRule lockAll(Set<IResource> resources) {
			return new MultiRule(resources.toArray(new ISchedulingRule[resources.size()]));
		}
		
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			clearExistingMarkers();
			addNewMarkers();
			return Status.OK_STATUS;
		}

		public void addNewMarkers() {
			int problemCount = 0;
			for (Entry<IResource, List<Problem>> entry : problems.entrySet()) {
				if(problemCount >= 100){
					return;
				}
				IResource resource = entry.getKey();
				for (Problem problem : entry.getValue()) {
					createMarker(resource, problem);
				}
			}
		}

		public void clearExistingMarkers() {
			scope.forEach(new Procedure<IResource>() {
				public void apply(IResource resource) {
					try{
						resource.deleteMarkers(RTEXT_MARKERS, true, IResource.DEPTH_INFINITE);
					} catch (CoreException e) {
						logError("Exception when deleting marker on :" + "workspace", e);
					}
				}
			});
		}

		public void createMarker(IResource resource, Problem problem){
			Map<Object, Object> attributes = new HashMap<Object, Object>();
			setLineNumber(attributes, problem.getLine());
			setMessage(attributes, problem.getMessage());
//			MarkerUtilities.setCharStart(attributes, 0);
//			MarkerUtilities.setCharEnd(attributes, 0);
			attributes.put(IMarker.SEVERITY, SEVERITY_MAP.get(problem.getSeverity()));
			attributes.put(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			try{
				MarkerUtilities.createMarker(resource, attributes , RTEXT_MARKERS);
			} catch (CoreException e) {
				logError("Exception when setting marker on :" + resource.getFullPath(), e);
			}
		}
	}
	
	@SuppressWarnings("serial")
	private static final Map<String, Integer> SEVERITY_MAP = new HashMap<String, Integer>(){{
		put("debug", IMarker.SEVERITY_INFO);
		put("info", IMarker.SEVERITY_INFO);
		put("warn", IMarker.SEVERITY_WARNING);
		put("error", IMarker.SEVERITY_ERROR);
		put("fatal", IMarker.SEVERITY_ERROR);
	}};
	
	private ProblemUpdateJobFactory jobFactory;
	private FileLocator fileLocator;
	private ConnectorScope scope;

	public static LoadModelCallback create(ConnectorConfig connectorConfig){
		ConnectorScope scope = ConnectorScope.create(connectorConfig);
		return new LoadModelCallback(new ProblemUpdateJobFactory(), new FileLocator(), scope);
	}
	
	public LoadModelCallback(ProblemUpdateJobFactory jobFactory, FileLocator fileLocator, ConnectorScope scope) {
		super("Loading model");
		this.jobFactory = jobFactory;
		this.fileLocator = fileLocator;
		this.scope = scope;
	}
	
	public void handleResponse(LoadedModel response) {
		Map<IResource, List<Problem>> problems = gatherProblems(response);
		runUpdateJob(problems);
		super.handleResponse(response);
	}

	public void runUpdateJob(Map<IResource, List<Problem>> problems) {
		ProblemUpdateJob updateJob = jobFactory.create(problems, scope);
		updateJob.schedule();
	}

	public Map<IResource, List<Problem>> gatherProblems(LoadedModel response) {
		Map<IResource, List<Problem>> problems = new HashMap<IResource, List<Problem>>();
		for (FileProblems fileProblems : response.getProblems()) {
			List<IFile> locatedFiles = fileLocator.locate(fileProblems.getFile());
			for (IFile locatedFile : locatedFiles) {
				problems.put(locatedFile, fileProblems.getProblems());
			}
		}
		return problems;
	}
}
