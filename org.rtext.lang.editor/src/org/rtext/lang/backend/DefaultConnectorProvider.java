/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.rtext.lang.util.Exceptions;


public class DefaultConnectorProvider implements ConnectorProvider{

	private final ConnectorFactory connectorProvider;
	private final Map<String, Connector> connectors = new HashMap<String, Connector>();
	
	public DefaultConnectorProvider(ConnectorFactory connectorProvider) {
		this.connectorProvider = connectorProvider;
	}
	
	protected Connector createConnector(ConnectorConfig input) {
		Connector connector = connectorProvider.createConnector(input);
		connectors.put(getKey(input.configFile), connector);
		return connector;
	}

	private String getKey(File configFile) {
		try {
			return configFile.getCanonicalPath();
		} catch (IOException e) {
			Exceptions.rethrow(e);
			return ""; // not reachable
		}
	}

	public void dispose() {
		for (Connector connector : connectors.values()) {
			connector.disconnect();
		}
	}

	public void dispose(String rtextFilePath) {
		Connector connector = connectors.get(getKey(new File(rtextFilePath)));
		if(connector == null){
			return;
		}
		connector.disconnect();
	}

	public synchronized Connector get(ConnectorConfig connectorConfig) {
		return createConnector(connectorConfig);
	}
}