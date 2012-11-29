package org.rtext.lang.workspace;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;

public class ModelLoadJob extends RTextJob {
	private final Connector connector;
	private ConnectorConfig config;

	public ModelLoadJob(Connector connector, ConnectorConfig config) {
		super("Connecting backend");
		this.connector = connector;
		this.config = config;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			connector.execute(new LoadModelCommand(), LoadModelCallback.create(config));
		} catch (Exception e) {
			return new Status(IStatus.ERROR, RTextPlugin.PLUGIN_ID, e.getMessage(), e);
		}
		return Status.OK_STATUS;
	}
}