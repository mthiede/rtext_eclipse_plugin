package org.rtext.lang.workspace;

import java.io.File;

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
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;

public class ReloadJob extends RTextJob{
		
		public static Job create(ConnectorProvider connectorProvider, IResource resource) {
			return new ReloadJob(connectorProvider, resource, new MarkerUtil());
		}

		private IResource resource;
		private ConnectorProvider connectorProvider;
		private MarkerUtil markerUtil;

		public ReloadJob(ConnectorProvider connectorProvider, IResource resource, MarkerUtil markerUtil) {
			super("Reloading RText backends");
			this.connectorProvider = connectorProvider;
			this.resource = resource;
			this.markerUtil = markerUtil;
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
			File configFile = new File(resource.getLocation().toString());
			RTextFile rTextFile = new RTextFileParser().doParse(configFile);
			monitor.beginTask("Reloading models", rTextFile.getConfigurations().size());
			for (ConnectorConfig config : rTextFile.getConfigurations()) {
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

		private void clearMarkers(ConnectorConfig config) {
			markerUtil.clearExistingMarkers(ConnectorScope.create(config));
		}

		private void reloadModels(ConnectorConfig config) {
			Connector connector = connectorProvider.get(config);
			connector.execute(new LoadModelCommand(), LoadModelCallback.create(config));
		}
	}