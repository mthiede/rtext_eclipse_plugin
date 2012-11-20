package org.rtext.lang.editor;

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
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.rtext.lang.backend2.LoadedModel;
import org.rtext.lang.backend2.LoadedModel.FileProblems;
import org.rtext.lang.backend2.LoadedModel.Problem;
import org.rtext.lang.backend2.WorkspaceCallback;

public class LoadModelCallback extends WorkspaceCallback<LoadedModel> {

	public static class ProblemUpdateJobFactory{
		public ProblemUpdateJob create(Map<IResource, List<Problem>> problems){
			return new ProblemUpdateJob(problems);
		}
	}
	
	public static class ProblemUpdateJob extends Job{

		private Map<IResource, List<Problem>> problems;

		public ProblemUpdateJob(Map<IResource, List<Problem>> problems) {
			super("Updating Problem markers");
			this.problems = problems;
			setRule(lockAll(problems.keySet()));
		}
		
		public MultiRule lockAll(Set<IResource> resources) {
			return new MultiRule(resources.toArray(new ISchedulingRule[resources.size()]));
		}
		
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			for (Entry<IResource, List<Problem>> entry : problems.entrySet()) {
				IResource resource = entry.getKey();
				try {
					resource.deleteMarkers("rtext", true, IResource.DEPTH_INFINITE);
				} catch (CoreException e) {
				}
				for (Problem problem : entry.getValue()) {
					try {
						IMarker marker = resource.createMarker("rtext");
						marker.setAttribute(IMarker.MESSAGE, problem.getMessage());
						marker.setAttribute(IMarker.LINE_NUMBER, problem.getLine());
						marker.setAttribute(IMarker.SEVERITY, SEVERITY_MAP.get(problem.getSeverity()));
						marker.setAttribute(IMarker.SOURCE_ID, "RText");
					} catch (CoreException e) {
					}
				}
			}
			return Status.OK_STATUS;
		}
		
	}
	
	@SuppressWarnings("serial")
	private static final Map<String, Integer> SEVERITY_MAP = new HashMap<String, Integer>(){{
		put("d", IMarker.SEVERITY_INFO);
		put("i", IMarker.SEVERITY_INFO);
		put("w", IMarker.SEVERITY_WARNING);
		put("e", IMarker.SEVERITY_ERROR);
		put("f", IMarker.SEVERITY_ERROR);	
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
		super.handleResponse(response);
		Map<IResource, List<Problem>> problems = gatherProblems(response);
		if(problems.isEmpty()){
			return;
		}
		runUpdateJob(problems);
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
