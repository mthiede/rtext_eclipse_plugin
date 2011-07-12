package org.rtext.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;
import org.rtext.RTextPlugin;

public class ConnectorManager {
	private static Map<String, Connector> connectorMap = new HashMap<String, Connector>();
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
				try {
					updateConnectors();
				}
				catch (Exception e) {
					RTextPlugin.getDefault().getLog().log(
					  new Status(Status.ERROR, RTextPlugin.PLUGIN_ID, Status.OK, 
					  	"exception in ConnectorUpdaterJob", e));
				}				
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
				try {
					handleProcessOutput();
				}
				catch (Exception e) {
					RTextPlugin.getDefault().getLog().log(
					  new Status(Status.ERROR, RTextPlugin.PLUGIN_ID, Status.OK, 
					  	"exception in ProcessOutputHandlerJob", e));
				}
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
		stopBackends();
		getConnectorUpdaterJob().stop();
		getProcessOutputHandlerJob().stop();
	}

	public static Connector getConnector(IPath file) {
		ConnectorConfig config = findConnectorConfig(file);
		if (config != null) {
			String key = config.getIdentifier();
			Connector bc = connectorMap.get(key);
			if (bc != null) {
				return bc;
			}
			else {
				bc = new Connector(config);
				connectorMap.put(key, bc);
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
	
	private static void stopBackends() {
		for (Iterator<Connector> it = connectorMap.values().iterator(); it.hasNext();) {
			Connector bc = (Connector) it.next();
			bc.executeCommand(new Command("stop", ""), 1);
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
	
	private static ConnectorConfig findConnectorConfig(IPath file) {
		IPath path = file;
		String specifierPattern = extractLanguageSpecifierPattern(file); 
		while (true) {
			path = path.removeLastSegments(1);
			if (path.segmentCount() > 0) {
				File descFile = path.append("/.rtext").toFile();
				if (descFile.exists()) {
					List<ConnectorConfig> configs = ConfigFileParser.parse(descFile);
					for (ConnectorConfig config : configs) {
						for (String pattern : config.getPatterns()) {
							if (pattern.trim().equals(specifierPattern)) {
								return config;
							}
						}
					}
				}
			}
			else {
				return null;
			}
		}
	}

	private static String extractLanguageSpecifierPattern(IPath file) {
		String extension = file.getFileExtension();
		if (extension != null) {
			return "*."+extension;
		}
		else {
			return file.lastSegment();
		}
	}
}
