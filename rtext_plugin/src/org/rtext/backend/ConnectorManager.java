package org.rtext.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;

public class ConnectorManager {
	private static Map<File, Connector> connectorMap = new HashMap<File, Connector>();
	private static ConnectorUpdaterJob periodicJob;
	private static ProcessOutputHandlerJob outputHandlerJob;
	
	private static class ConnectorUpdaterJob extends UIJob {
		private static long UpdateCycle = 100;
		private boolean stop = false;
		
		public ConnectorUpdaterJob() {
			super("RText Backend Connector Job");
			setSystem(true);
		}
		
		public void start() {
			schedule(UpdateCycle);
		}
	
		public void stop() {
			stop = true;
		}
	
		public IStatus runInUIThread(IProgressMonitor monitor) {
			if (!stop) {
				updateConnectors();
				schedule(UpdateCycle);
			}
			return Status.OK_STATUS;
		}
	}

	private static class ProcessOutputHandlerJob extends Job {
		private static long UpdateCycle = 100;
		private boolean stop = false;
		
		public ProcessOutputHandlerJob() {
			super("RText Process Output Handler Job");
			setSystem(true);
		}
		
		public void start() {
			schedule(UpdateCycle);
		}
	
		public void stop() {
			stop = true;
		}
	
		protected IStatus run(IProgressMonitor monitor) {
			if (!stop) {
				handleProcessOutput();
				schedule(UpdateCycle);
			}
			return Status.OK_STATUS;
		}
	}

	public static void start() {
		getConnectorUpdaterJob().start();
		getProcessOutputHandlerJob().start();
	}

	public static void stop() {
		getConnectorUpdaterJob().stop();
		getProcessOutputHandlerJob().stop();
		disposeConnectors();
	}

	public static Connector getConnector(IPath file) {
		File descFile = findDescriptorFileFor(file);
		if (descFile != null) {
			Connector bc = connectorMap.get(descFile);
			if (bc != null) {
				return bc;
			}
			else {
				bc = new Connector(descFile);
				connectorMap.put(descFile, bc);
				return bc;
			}
		}
		else {
			return null;
		}
	}

	private static void updateConnectors() {
		for (Iterator<Connector> it = connectorMap.values().iterator(); it.hasNext();) {
			Connector bc = (Connector) it.next();
			bc.updateConnector();
		} 
	}
	
	private static void handleProcessOutput() {
		for (Iterator<Connector> it = connectorMap.values().iterator(); it.hasNext();) {
			Connector bc = (Connector) it.next();
			bc.handleProcessOutput();
		} 
	}
	
	private static void disposeConnectors() {
		for (Iterator<Connector> it = connectorMap.values().iterator(); it.hasNext();) {
			Connector bc = (Connector) it.next();
			bc.killProcess();
		} 		
	}

	private static ConnectorUpdaterJob getConnectorUpdaterJob() {
		if (periodicJob == null) {
			periodicJob = new ConnectorUpdaterJob();
		}
		return periodicJob;
	}

	private static ProcessOutputHandlerJob getProcessOutputHandlerJob() {
		if (outputHandlerJob == null) {
			outputHandlerJob = new ProcessOutputHandlerJob();
		}
		return outputHandlerJob;
	}
	
	static File findDescriptorFileFor(IPath file) {
		IPath path = file;
		while (true) {
			path = path.removeLastSegments(1);
			if (path.segmentCount() > 0) {
				File descFile = path.append("/.rtext").toFile();
				if (descFile.exists()) {
					return descFile;
				}
			}
			else {
				return null;
			}
		}
	}

}
