package org.rtext.lang.backend2;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.backend.ConnectorConfig;

public interface BackendStarter {
	public void startProcess(ConnectorConfig connectorConfig) throws TimeoutException;
	public boolean isRunning();
	public void stop();
	int getPort() throws TimeoutException;
}
		