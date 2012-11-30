package org.rtext.lang.workspace;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.rtext.lang.backend.Connector;

public class BackendConnectJob extends RTextJob {
	
	public static class Factory{
		public BackendConnectJob create(Connector connector){
			return new BackendConnectJob(connector);
		}
	}
	
	private final Connector connector;

	public BackendConnectJob(Connector connector) {
		super("Connecting backend");
		this.connector = connector;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		connector.connect();
		return Status.OK_STATUS;
	}
	
	public void execute(){
		schedule(100);
	}

	public Boolean isRunning(){
		return getState() != NONE;
	}
}