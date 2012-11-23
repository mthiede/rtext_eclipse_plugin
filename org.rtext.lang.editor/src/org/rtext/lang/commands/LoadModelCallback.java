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
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.rtext.lang.commands.LoadedModel.FileProblems;
import org.rtext.lang.commands.LoadedModel.Problem;
import org.rtext.lang.util.FileLocator;
import org.rtext.lang.util.RTextJob;

public class LoadModelCallback extends WorkspaceCallback<LoadedModel> {

	public static final String RTEXT_MARKERS = "org.rtext.lang.editor.makers";
	public static final String PROBLEM_MARKER_JOB = "Updating Problem markers";

	public static class ProblemUpdateJobFactory{
		public ProblemUpdateJob create(Map<IResource, List<Problem>> problems){
			return new ProblemUpdateJob(problems);
		}
	}
	
	public static class ProblemUpdateJob extends RTextJob{
		private Map<IResource, List<Problem>> problems;

		public ProblemUpdateJob(Map<IResource, List<Problem>> problems) {
			super(PROBLEM_MARKER_JOB);
			this.problems = problems;
			setRule(lockAll(problems.keySet()));
		}
		
		public MultiRule lockAll(Set<IResource> resources) {
			return new MultiRule(resources.toArray(new ISchedulingRule[resources.size()]));
		}
		
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				IWorkspaceRoot resource = ResourcesPlugin.getWorkspace().getRoot();
				resource.deleteMarkers(RTEXT_MARKERS, true, IResource.DEPTH_INFINITE);
			} catch (CoreException e) {
				logError("Exception when deleting marker on :" + "workspace", e);
			}
			int problemCount = 0;
			for (Entry<IResource, List<Problem>> entry : problems.entrySet()) {
				if(problemCount >= 100){
					return Status.OK_STATUS;
				}
				IResource resource = entry.getKey();
				for (Problem problem : entry.getValue()) {
					createMarker(resource, problem);
				}
			}
			return Status.OK_STATUS;
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

	public static LoadModelCallback create(){
		return new LoadModelCallback(new ProblemUpdateJobFactory(), new FileLocator());
	}
	
	public LoadModelCallback(ProblemUpdateJobFactory jobFactory, FileLocator fileLocator) {
		super("Loading model");
		this.jobFactory = jobFactory;
		this.fileLocator = fileLocator;
	}
	
	public void handleResponse(LoadedModel response) {
		Map<IResource, List<Problem>> problems = gatherProblems(response);
		runUpdateJob(problems);
		super.handleResponse(response);
	}

	public void runUpdateJob(Map<IResource, List<Problem>> problems) {
		ProblemUpdateJob updateJob = jobFactory.create(problems);
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
