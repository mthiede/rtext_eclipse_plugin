package org.rtext.lang.workspace;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;

public class ReloadJob extends RTextJob {

	public static Job create(ConnectorProvider connectorProvider, Map<ConnectorConfig, Boolean> configs) {
		return new ReloadJob(configs, connectorProvider, new MarkerUtil());
	}
	
	private MarkerUtil markerUtil;
	
	private Map<ConnectorConfig, Boolean> configurations;
	private ConnectorProvider connectorProvider;

	public ReloadJob(Map<ConnectorConfig, Boolean> configs, ConnectorProvider connectorProvider,
			MarkerUtil markerUtil) {
		super("Reloading RText backends");
		this.configurations = configs;
		this.connectorProvider = connectorProvider;
		this.markerUtil = markerUtil;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		System.out.println("Starting reload job");
		monitor.beginTask("Reloading models", configurations.size());
		for (Entry<ConnectorConfig, Boolean> entry : configurations.entrySet()) {
			try {
				ConnectorConfig config = entry.getKey();
				clearMarkers(config);
				Connector connector = connectorProvider.get(config);
				if(entry.getValue()){
					connector.disconnect();
				}
				reloadModels(connector, config);
				monitor.worked(1);
			} catch (Throwable e) {
				RTextPlugin.logError(e.getMessage(), e);
			}
		}
		monitor.done();
		return Status.OK_STATUS;
	}

	
	private void clearMarkers(ConnectorConfig config) {
		markerUtil.clearExistingMarkers(ConnectorScope.create(config));
	}

	private void reloadModels(Connector connector, ConnectorConfig config) {
		connector.execute(new LoadModelCommand(),
				LoadModelCallback.create(config));
	}
}