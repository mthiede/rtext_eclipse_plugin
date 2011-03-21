package rtext.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

public class ConnectorManager {
	private static Map<File, Connector> connectorMap = new HashMap<File, Connector>();
	private static PeriodicJob periodicJob;
	
	private static class PeriodicJob extends UIJob {
		private static long UpdateCycle = 1000;
		private boolean stop = false;
		
		public PeriodicJob() {
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

	public static void start() {
		getPeriodicJob().start();
	}

	public static void stop() {
		getPeriodicJob().stop();
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
			bc.update();
		} 
	}

	private static void disposeConnectors() {
		for (Iterator<Connector> it = connectorMap.values().iterator(); it.hasNext();) {
			Connector bc = (Connector) it.next();
			bc.killProcess();
		} 		
	}

	private static PeriodicJob getPeriodicJob() {
		if (periodicJob == null) {
			periodicJob = new PeriodicJob();
		}
		return periodicJob;
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
