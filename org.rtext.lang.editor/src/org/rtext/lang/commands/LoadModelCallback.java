/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.commands.LoadedModel.FileProblems;
import org.rtext.lang.commands.LoadedModel.Problem;
import org.rtext.lang.util.FileLocator;
import org.rtext.lang.workspace.MarkerUtil;
import org.rtext.lang.workspace.RTextJob;

public class LoadModelCallback extends WorkspaceCallback<LoadedModel> {

	public static final String PROBLEM_MARKER_JOB = "Updating Problem markers";
	
	public static class ProblemUpdateJobFactory{
		public ProblemUpdateJob create(Map<IResource, List<Problem>> problems, ConnectorScope scope){
			return new ProblemUpdateJob(problems, scope);
		}
	}
	
	public static class ProblemUpdateJob extends RTextJob{
		private Map<IResource, List<Problem>> problems;
		private MarkerUtil markerUtil = new MarkerUtil();

		public ProblemUpdateJob(Map<IResource, List<Problem>> problems, ConnectorScope scope) {
			super(PROBLEM_MARKER_JOB);
			this.problems = problems;
			setRule(lockAll(problems.keySet()));
		}
		
		public MultiRule lockAll(Set<IResource> resources) {
			return new MultiRule(resources.toArray(new ISchedulingRule[resources.size()]));
		}
		
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			addNewMarkers(monitor);
			return Status.OK_STATUS;
		}

		public void addNewMarkers(IProgressMonitor monitor) {
			monitor.beginTask(PROBLEM_MARKER_JOB, problems.size());
			for (Entry<IResource, List<Problem>> entry : problems.entrySet()) {
				IResource resource = entry.getKey();
				markerUtil.clearExistingMarkers(resource);
				addMarkers(entry, resource);
				monitor.worked(1);
			}
			monitor.done();
		}

		private void addMarkers(Entry<IResource, List<Problem>> entry,
				IResource resource) {
			int problemCount = 0;
			for (Problem problem : entry.getValue()) {
				if(problemCount >= 100){
					break;
				}
				problemCount++;
				markerUtil.createMarker(resource, problem);
			}
		}
	}
	
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
