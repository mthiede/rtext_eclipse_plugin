package org.rtext.lang.workspace;

import static org.rtext.lang.backend.RTextFile.RTEXT_FILE_NAME;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.ConnectorProvider;

public class RTextFileChangeListener implements IResourceChangeListener {

	public static RTextFileChangeListener create(ConnectorProvider connectorProvider) {
		return new RTextFileChangeListener(connectorProvider);
	}

	private ConnectorProvider connectorProvider;

	public RTextFileChangeListener(ConnectorProvider connectorProvider) {
		this.connectorProvider = connectorProvider;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if(event.getDelta() == null) return;
		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					handleRTextFileChange(delta);
					return true;
				}
			});
		} catch (CoreException e) {
			RTextPlugin
					.logError("Exception when parsing .rtext file change", e);
		}

	}

	private void handleRTextFileChange(IResourceDelta delta) {
		if (isAdded(delta)) {
			return;
		}
		IResource resource = delta.getResource();
		if (isNotRTextFile(resource)) {
			return;
		}
		if (hasNotChanged(delta))
			return;
		if(resource.getLocation() == null){
			return;
		}
		runCleanupJob(resource);
	}

	private void runCleanupJob(IResource resource) {
		ReloadJob.create(connectorProvider, resource).schedule(200);
	}

	public boolean hasNotChanged(IResourceDelta delta) {
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
