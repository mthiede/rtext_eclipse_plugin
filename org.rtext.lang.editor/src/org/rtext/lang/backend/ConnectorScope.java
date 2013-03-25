package org.rtext.lang.backend;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.util.Expectations;
import org.rtext.lang.util.Procedure;
import org.rtext.lang.util.StopWatch;

public class ConnectorScope {
	
	public static ConnectorScope create(ConnectorConfig connectorConfig){
		Expectations.expectNotNull(connectorConfig);
		return new ConnectorScope(ResourcesPlugin.getWorkspace().getRoot(), connectorConfig);
	}

	private ConnectorConfig connectorConfig;
	private IWorkspaceRoot workspace;

	public ConnectorScope(IWorkspaceRoot workspace, ConnectorConfig connectorConfig) {
		this.workspace = workspace;
		this.connectorConfig = connectorConfig;
	}

	public void forEach(final Procedure<IResource> handler) {
		StopWatch stopWatch = new StopWatch();
		final Path connectorRootFolder = new Path(connectorConfig.getConfigFile().getParent());
		try {
			workspace.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					IContainer parentFolder = getParentFolder(resource);
					
					if(parentFolder == null){
						return true;
					}
					if(parentFolder.getLocation() == null){
						return false;
					}
					if(!connectorRootFolder.isPrefixOf(parentFolder.getLocation())){
						return true;
					}
					if(connectorConfig.matches(resource.getName())){
						handler.apply(resource);
					}
					return true;
				}
			});
		} catch (CoreException e) {
			RTextPlugin.logError(e.getMessage(), e);
		}
		stopWatch.stop("Calculating connector scope");
	}
	
	private IContainer getParentFolder(IResource resource) {
		IContainer parentFolder = resource.getParent();
		while(parentFolder != null && parentFolder.isVirtual()){
			parentFolder = parentFolder.getParent();
		}
		return parentFolder;
	}
}
