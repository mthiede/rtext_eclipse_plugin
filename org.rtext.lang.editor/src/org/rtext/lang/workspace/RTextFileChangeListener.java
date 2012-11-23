package org.rtext.lang.workspace;

import static org.rtext.lang.backend.RTextFile.RTEXT_FILE_NAME;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.util.RTextJob;

public class RTextFileChangeListener implements IResourceChangeListener {

	public class CleanUpJob extends RTextJob{

		private IResource resource;

		public CleanUpJob(IResource resource) {
			super("Reloading RText backends");
			this.resource = resource;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			connectorProvider.dispose(resource.getLocation().toString());
			if(resource.exists()){
				triggerReload(resource);
			}
			return Status.OK_STATUS;
		}

	}

	public static RTextFileChangeListener create(ConnectorProvider connectorProvider) {
		return new RTextFileChangeListener(connectorProvider);
	}

	private ConnectorProvider connectorProvider;

	public RTextFileChangeListener(ConnectorProvider connectorProvider) {
		this.connectorProvider = connectorProvider;
	}

	public void resourceChanged(IResourceChangeEvent event) {
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
		new CleanUpJob(resource).schedule();
	}

	private void triggerReload(IResource resource) {
		File configFile = new File(resource.getLocation().toString());
		RTextFile rTextFile = new RTextFileParser().doParse(configFile);
		List<Connector> connectors = connectorProvider.get(rTextFile);
		for (Connector connector : connectors) {
			connector.execute(new LoadModelCommand(), LoadModelCallback.create());
		}
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
