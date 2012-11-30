package org.rtext.lang.workspace;

import static org.rtext.lang.backend.RTextFile.RTEXT_FILE_NAME;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.PlatformUI;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.editor.RTextEditor;

public class RTextFileChangeListener implements IResourceChangeListener {

	public static RTextFileChangeListener create(ConnectorProvider connectorProvider) {
		return new RTextFileChangeListener(connectorProvider);
	}

	private ConnectorProvider connectorProvider;
	private Set<String> registeredRTextExtensions;

	public RTextFileChangeListener(ConnectorProvider connectorProvider) {
		this.connectorProvider = connectorProvider;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if(event.getDelta() == null) return;
		initRTextExtensions();
		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					handleRTextFileChange(delta);
					return true;
				}
			});
		} catch (CoreException e) {
			RTextPlugin.logError("Exception when parsing .rtext file change", e);
		}

	}

	private void initRTextExtensions() {
		IFileEditorMapping[] fileEditorMappings = PlatformUI.getWorkbench().getEditorRegistry().getFileEditorMappings();
		registeredRTextExtensions = new HashSet<String>(fileEditorMappings.length);
		for (IFileEditorMapping editorMapping : fileEditorMappings) {
			IEditorDescriptor editor = editorMapping.getDefaultEditor();
			if(editor != null && RTextEditor.RTEXT_EDITOR_ID.equals(editor.getId())){
				registeredRTextExtensions.add(editorMapping.getExtension());
			}
		}
	}

	private void handleRTextFileChange(IResourceDelta delta) {
		if (isAdded(delta)) {
			return;
		}
		if (hasNotChanged(delta)){
			return;
		}
		IResource resource = delta.getResource();
		if(isModelFile(resource)){
			runCleanupJob(resource);
		}
		if (isNotRTextFile(resource)) {
			return;
		}
		if(resource.getLocation() == null){
			return;
		}
		runCleanupJob(resource);
	}

	private boolean isModelFile(IResource resource) {
		return registeredRTextExtensions.contains(resource.getFileExtension());
	}

	private void runCleanupJob(IResource resource) {
		ReloadJob.create(connectorProvider, resource).schedule(200);
	}

	public boolean hasNotChanged(IResourceDelta delta) {
		int flags = delta.getFlags();
	      if ((flags & IResourceDelta.CONTENT) != 0) {
	         return false;
	      }
	      if ((flags & IResourceDelta.REPLACED) != 0) {
	    	  return false;
	      }
	      if ((flags & IResourceDelta.MARKERS) != 0) {
	          return true;
	      }
		return delta.getKind() != IResourceDelta.CHANGED && delta.getKind() != IResourceDelta.REMOVED;
	}

	public boolean contentHasNotChanged(IResourceDelta delta) {
		return (delta.getFlags() & IResourceDelta.CONTENT) == 0;
	}

	private boolean isNotRTextFile(IResource resource) {
		return resource == null || !RTEXT_FILE_NAME.equals(resource.getName());
	}

	private boolean isAdded(IResourceDelta delta) {
		return delta.getKind() == IResourceDelta.ADDED;
	}

}
