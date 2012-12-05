package org.rtext.lang.workspace;

import static org.eclipse.ui.texteditor.MarkerUtilities.setLineNumber;
import static org.eclipse.ui.texteditor.MarkerUtilities.setMessage;
import static org.rtext.lang.RTextPlugin.logError;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.commands.LoadedModel.Problem;
import org.rtext.lang.util.Procedure;

public class MarkerUtil {
	
	public static final String RTEXT_MARKERS = "org.rtext.lang.editor.makers";

	@SuppressWarnings("serial")
	private static final Map<String, Integer> SEVERITY_MAP = new HashMap<String, Integer>(){{
		put("debug", IMarker.SEVERITY_INFO);
		put("info", IMarker.SEVERITY_INFO);
		put("warn", IMarker.SEVERITY_WARNING);
		put("error", IMarker.SEVERITY_ERROR);
		put("fatal", IMarker.SEVERITY_ERROR);
	}};
	
	public void createMarker(IResource resource, Problem problem){
		if(cannotAcceptMarker(resource)){
			return;
		}
		Map<Object, Object> attributes = new HashMap<Object, Object>();
		setLineNumber(attributes, problem.getLine());
		setMessage(attributes, problem.getMessage());
//		MarkerUtilities.setCharStart(attributes, 0);
//		MarkerUtilities.setCharEnd(attributes, 0);
		attributes.put(IMarker.SEVERITY, SEVERITY_MAP.get(problem.getSeverity()));
		attributes.put(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		try{
			MarkerUtilities.createMarker(resource, attributes , RTEXT_MARKERS);
		} catch (CoreException e) {
			logError("Exception when setting marker on :" + resource.getFullPath(), e);
		}
	}

	private boolean cannotAcceptMarker(IResource resource) {
		return !resource.isAccessible();
	}
	
	public void clearExistingMarkers(ConnectorScope scope) {
		scope.forEach(new Procedure<IResource>() {
			public void apply(IResource resource) {
				try{
					if(cannotAcceptMarker(resource)){
						return;
					}
					resource.deleteMarkers(MarkerUtil.RTEXT_MARKERS, true, IResource.DEPTH_INFINITE);
				} catch (CoreException e) {
					logError("Exception when deleting marker on :" + "workspace", e);
				}
			}
		});
	}

}
