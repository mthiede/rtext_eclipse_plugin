package rtext.editor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.texteditor.MarkerUtilities;

import rtext.backend.Command;
import rtext.backend.Connector;
import rtext.backend.ConnectorManager;
import rtext.backend.IResponseListener;

public class ProblemUpdater implements IResponseListener {
	private IPath path;
	
	public ProblemUpdater(IPath path) {
		this.path = path;
	}
	
	public void updateProblems()
	{
		Connector bc = ConnectorManager.getConnector(path);
		if (bc != null) {
			bc.executeCommand(
				new Command("show_problems", path.toString()), 1, this);
		}
	}
	
	private void deleteProblems(IFile wsFile) {
		try {
			wsFile.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
	
	private void showProblem(String desc, IFile wsFile) {
		StringTokenizer st = new StringTokenizer(desc, ";");
		String line = st.nextToken();
		String message = st.nextToken();
		if (line != null && message != null) {
			Map<String, Object> map = new HashMap<String, Object>();
		    MarkerUtilities.setLineNumber(map, Integer.valueOf(line));
		    MarkerUtilities.setMessage(map, message);
		    map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
		    if (wsFile != null) {
				try {
					MarkerUtilities.createMarker(wsFile, map, IMarker.PROBLEM);
				} catch (CoreException e) {
				}
		    }
		}
	}
	
	public void responseReceived(StringTokenizer st) {
	    IFile wsFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
	    deleteProblems(wsFile);
	    while (st.hasMoreTokens()) {
			showProblem(st.nextToken(), wsFile);
		}
	}

}
