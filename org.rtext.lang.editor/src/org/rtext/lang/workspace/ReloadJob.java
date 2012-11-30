package org.rtext.lang.workspace;

import static org.rtext.lang.backend.RTextFile.RTEXT_FILE_NAME;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.backend.FileSystemBasedConfigProvider;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;

public class ReloadJob extends RTextJob{
		
		public static Job create(ConnectorProvider connectorProvider, IResource resource) {
			return new ReloadJob(connectorProvider, resource, new MarkerUtil(), FileSystemBasedConfigProvider.create());
		}

		private IResource resource;
		private ConnectorProvider connectorProvider;
		private MarkerUtil markerUtil;
		private FileSystemBasedConfigProvider configProvider;

		public ReloadJob(ConnectorProvider connectorProvider, IResource resource, MarkerUtil markerUtil, FileSystemBasedConfigProvider configProvider) {
			super("Reloading RText backends");
			this.connectorProvider = connectorProvider;
			this.resource = resource;
			this.markerUtil = markerUtil;
			this.configProvider = configProvider;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			connectorProvider.dispose(resource.getLocation().toString());
			if(!resource.exists()){
				return Status.OK_STATUS;
			}
			triggerReload(resource, monitor);
			return Status.OK_STATUS;
		}
		
		private void triggerReload(IResource resource, IProgressMonitor monitor) {
			Collection<ConnectorConfig> configurations = getConfigurations(resource);
			monitor.beginTask("Reloading models", configurations.size());
			for (ConnectorConfig config : configurations) {
				try{
					clearMarkers(config);
					reloadModels(config);
					monitor.worked(1);
				}catch(Throwable e){
					RTextPlugin.logError(e.getMessage(), e);
				}
			}
			monitor.done();
		}

		private Collection<ConnectorConfig> getConfigurations(IResource resource) {
			Collection<ConnectorConfig> configurations;
			if(RTEXT_FILE_NAME.equals(resource.getName())){
				File configFile = new File(resource.getLocation().toString());
				RTextFile rTextFile = new RTextFileParser().doParse(configFile);
				configurations = rTextFile.getConfigurations();
			}else{
				ConnectorConfig config = configProvider.get(resource.getLocation().toFile().toString());
				configurations = Collections.singletonList(config);
			}
			return configurations;
		}

		private void clearMarkers(ConnectorConfig config) {
			markerUtil.clearExistingMarkers(ConnectorScope.create(config));
		}

		private void reloadModels(ConnectorConfig config) {
			Connector connector = connectorProvider.get(config);
			connector.execute(new LoadModelCommand(), LoadModelCallback.create(config));
		}
	}