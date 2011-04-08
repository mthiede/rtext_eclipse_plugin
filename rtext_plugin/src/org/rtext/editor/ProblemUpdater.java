package org.rtext.editor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.rtext.backend.Command;
import org.rtext.backend.Connector;
import org.rtext.backend.ConnectorManager;
import org.rtext.backend.IResponseListener;


public class ProblemUpdater implements IResponseListener {
	private Connector connector;
	
	public ProblemUpdater(IPath path) {
		connector = ConnectorManager.getConnector(path);
	}
	
	public void updateProblems()
	{
		if (connector != null) {
			connector.executeCommand(new Command("show_problems", ""), this, 60000);
		}
	}
	
	private void deleteProblems() {
		try {
			Path path = new Path(connector.getConfig().getConfigFile().getAbsolutePath());
			IFile wsFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
			IMarker[] markers = wsFile.getParent().findMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
			for (IMarker marker : markers) {
				Object sourceId = marker.getAttribute(IMarker.SOURCE_ID);
				if (sourceId != null && sourceId.equals(connector.getConfig().getIdentifier())) {
					marker.delete();
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
	
	private void showProblem(String desc, IFile wsFile) {
		StringTokenizer st = new StringTokenizer(desc, ";");
		if (st.countTokens() == 2) {
			String line = st.nextToken();
			String message = st.nextToken();
			if (line != null && message != null) {
				Map<String, Object> map = new HashMap<String, Object>();
			    MarkerUtilities.setLineNumber(map, Integer.valueOf(line));
			    MarkerUtilities.setMessage(map, message);
			    map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
			    map.put(IMarker.SOURCE_ID, connector.getConfig().getIdentifier());
			    if (wsFile != null) {
					try {
						MarkerUtilities.createMarker(wsFile, map, IMarker.PROBLEM);
					} catch (CoreException e) {
					}
			    }
			}
		}
	}
	
	public void responseReceived(StringTokenizer st) {
		deleteProblems();
	    IFile wsFile = null;
	    while (st.hasMoreTokens()) {
	    	String line = st.nextToken();
	    	if (line.split(";").length == 1) {
	    	    wsFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(line));
	    	}
	    	else if (wsFile != null) {
	    		showProblem(line, wsFile);
	    	}
		}
	}
	
	public void requestTimedOut() {
		
	}

}
