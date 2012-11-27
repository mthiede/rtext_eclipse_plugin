package org.rtext.lang.backend;

import static org.rtext.lang.util.Files.isSubdir;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.util.Expectations;
import org.rtext.lang.util.FileLocator;
import org.rtext.lang.util.Procedure;

public class ConnectorScope {
	
	public static ConnectorScope create(ConnectorConfig connectorConfig){
		Expectations.expectNotNull(connectorConfig);
		return new ConnectorScope(ResourcesPlugin.getWorkspace().getRoot(), connectorConfig, new FileLocator());
	}

	private ConnectorConfig connectorConfig;
	private IWorkspaceRoot workspace;
	private FileLocator fileLocator;

	public ConnectorScope(IWorkspaceRoot workspace, ConnectorConfig connectorConfig, FileLocator fileLocator) {
		this.workspace = workspace;
		this.connectorConfig = connectorConfig;
		this.fileLocator = fileLocator;
	}


	public void forEach(final Procedure<IResource> handler) {
		List<IFile> files = fileLocator.locate(configFile());
		if(files.isEmpty()){
			findInProjects(handler);
		}else{
			for (IFile file : files) {
				handle(handler, file.getParent());
			}
		}
	}

	private List<IContainer> findInProjects(Procedure<IResource> handler) {
		List<IContainer> result = new ArrayList<IContainer>();
		for (IProject project : workspace.getProjects()) {
			String projectLocation = project.getLocation().toString();
			if(isSubdir(connectorConfig.getConfigFile().getParent(), projectLocation)){
				handle(handler, project);
			}
		}
		return result;
	}

	public String configFile() {
		return connectorConfig.getConfigFile().toString();
	}

	public void handle(final Procedure<IResource> handler, IContainer container) {
		try {
			container.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if(connectorConfig.matches(resource.getName())){
						handler.apply(resource);
					}
					return true;
				}
			});
		} catch (CoreException e) {
			RTextPlugin.logError(e.getMessage(), e);
		}
	}

}
