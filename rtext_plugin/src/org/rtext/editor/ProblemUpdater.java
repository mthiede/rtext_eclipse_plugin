package org.rtext.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.rtext.backend.Command;
import org.rtext.backend.Connector;
import org.rtext.backend.IResponseListener;

public class ProblemUpdater extends Job implements IResponseListener {
	private Connector connector;
	private IStatusLineManager statusLineManager;
	private HashMap<String, Integer> severityMap;
	private IProgressMonitor progressMonitor;
	private int lastProgress;

	public ProblemUpdater(Connector connector, IStatusLineManager statusLineManager) {
		super("Loading Model");
		this.connector = connector;
		this.statusLineManager = statusLineManager;
		severityMap = new HashMap<String, Integer>();
		severityMap.put("d", new Integer(IMarker.SEVERITY_INFO));
		severityMap.put("i", new Integer(IMarker.SEVERITY_INFO));
		severityMap.put("w", new Integer(IMarker.SEVERITY_WARNING));
		severityMap.put("e", new Integer(IMarker.SEVERITY_ERROR));
		severityMap.put("f", new Integer(IMarker.SEVERITY_ERROR));		
	}

	protected IStatus run(IProgressMonitor monitor) {
		progressMonitor = monitor;
		lastProgress = 0;
		if (connector != null) {
			if (connector.getProtocolVersion() >= 1) {
				connector.executeCommand(new Command("show_problems2", ""), this, 300000);
				progressMonitor.beginTask("update problems", 100);
			}
			else {
				connector.executeCommand(new Command("show_problems", ""), this, 300000);
				progressMonitor.beginTask("update problems", IProgressMonitor.UNKNOWN);
			}
		}
		return Job.ASYNC_FINISH;
	}

	private void initProblems(HashMap<String, IMarker> problems) {
		try {
			Path configFilePath = new Path(connector.getConfig().getConfigFile().getAbsolutePath());
			IFile configFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(configFilePath);
			IContainer markerScope;
			if (configFile != null) {
				markerScope = configFile.getParent();
			}
			else {
				// if the config file is not in the workspace, search for markers in the whole workspace
				// this strategy will be inefficient in case only one out of many projects in the workspace
				// is located below the config file in the file system
				markerScope = ResourcesPlugin.getWorkspace().getRoot();
			}
			IMarker[] markers = markerScope.findMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
			HashSet<String> extensions = new HashSet<String>();
			for (String pattern : connector.getConfig().getPatterns()) {
				// the pattern should be *.ext
				String[] parts = pattern.trim().split("\\.");
				if (parts.length == 2) {
					extensions.add(parts[1]);
				}
			}			
			for (IMarker marker : markers) {
				Object sourceId = marker.getAttribute(IMarker.SOURCE_ID);
				if (sourceId != null && sourceId.equals("RText") && extensions.contains(marker.getResource().getFileExtension())) {
					String message = (String)marker.getAttribute(IMarker.MESSAGE);
					Integer line = (Integer)marker.getAttribute(IMarker.LINE_NUMBER);
					Integer severity = (Integer)marker.getAttribute(IMarker.SEVERITY);
					problems.put(problemKey(message, line.intValue(), severity, marker.getResource().getFullPath()), marker);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}

	private int problemLineNumber(String desc) {
		int index = desc.indexOf(';');
		if (connector.getProtocolVersion() >= 1 && index >= 0) {
			desc = desc.substring(index+1);
			index = desc.indexOf(';');
		}
		if (index >= 0) {
			return Integer.parseInt(desc.substring(0, index));
		}
		else {
			return -1;
		}
	}

	private String problemMessage(String desc) {
		int index = desc.indexOf(';');
		if (connector.getProtocolVersion() >= 1 && index >= 0) {
			desc = desc.substring(index + 1);
			index = desc.indexOf(';');
		}
		if (index >= 0) {
			return desc.substring(index + 1);
		}
		else {
			return null;
		}
	}

	private Integer problemSeverity(String desc) {
		int index = desc.indexOf(';');
		Integer severity = null;
		if (connector.getProtocolVersion() >= 1 && index >= 0) {
			String sevString = desc.substring(0, index);
			if (sevString != null) {
				severity = severityMap.get(sevString);					
			}
		}
		if (severity == null) {
			severity = new Integer(IMarker.SEVERITY_ERROR);
		}
		return severity;
	}

	private String problemKey(String message, int line, Integer severity, IPath fullPath) {
		return fullPath.toString()+"|"+String.valueOf(severity)+"|"+String.valueOf(line)+"|"+message;
	}

	private void addProblem(HashMap<String, IMarker> problems, String message, int line, Integer severity, IFile wsFile) {
		try {
			IMarker marker = wsFile.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.LINE_NUMBER, new Integer(line));
			marker.setAttribute(IMarker.SEVERITY, severity);
			marker.setAttribute(IMarker.SOURCE_ID, "RText");
			problems.put(problemKey(message, line, severity, wsFile.getFullPath()), marker);
		} catch (CoreException e) {
		}
	}

	public void responseReceived(List<String> responseLines) {
		HashMap<String, IMarker> problems = new HashMap<String, IMarker>();
		initProblems(problems);
		HashMap<String, IMarker> toBeRemoved = (HashMap<String, IMarker>)problems.clone();
		IFile[] wsFiles = null;
		int numProblems = 0;
		for (String line : responseLines) {
			if (!line.startsWith("progress:")) {
				if (line.split(";").length == 1) {
					wsFiles = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(line));
				}
				else if (wsFiles != null) {
					String message = problemMessage(line);
					int lineNumber = problemLineNumber(line);
					Integer severity = problemSeverity(line);
					numProblems++;
					if (message != null && lineNumber >= 0) {
						for (IFile file : wsFiles) {
							String key = problemKey(message, lineNumber, severity, file.getFullPath());
							if (problems.get(key) != null) {
								toBeRemoved.remove(key);
							}
							else {
								addProblem(problems, message, lineNumber, severity, file);
							}
						}
					}
				}
				else {
					// ignore progress information
				}
			}
		}
		for (String key : toBeRemoved.keySet()) {
			try {
				toBeRemoved.get(key).delete();
			} catch (CoreException e) {
				// can not delete marker
			}
			problems.remove(key);
		}
		statusLineManager.setMessage("Model loaded. " + String.valueOf(numProblems) + " problems.");
		progressMonitor.done();
		done(Status.OK_STATUS);
	}

	public void responseUpdate(List<String> responseLines) {
		if (connector.getProtocolVersion() >= 1) {
			int progress = getProgress(responseLines);
			if (progress >= 0) {
				progressMonitor.worked(progress - lastProgress);
				lastProgress = progress;
			}
		}
	}

	private int getProgress(List<String> responseLines) {
		String line = responseLines.get(responseLines.size()-1);
		int progress = -1;
		if (line.startsWith("progress:")) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				progress = Integer.parseInt(parts[1].trim());
				if (progress < 0) {
					progress = 0;
				}
				else if (progress > 100) {
					progress = 100;
				}
			}
		}
		return progress;
	}

	public void requestTimedOut() {
		statusLineManager.setMessage("Model loading timed out.");
		progressMonitor.done();
		done(Status.OK_STATUS);		
	}

}
