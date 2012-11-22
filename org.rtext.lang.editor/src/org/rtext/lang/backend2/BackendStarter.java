/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend2;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.backend.ConnectorConfig;

public interface BackendStarter {
	public void startProcess(ConnectorConfig connectorConfig) throws TimeoutException;
	public boolean isRunning();
	public void stop();
	int getPort() throws TimeoutException;
}
		