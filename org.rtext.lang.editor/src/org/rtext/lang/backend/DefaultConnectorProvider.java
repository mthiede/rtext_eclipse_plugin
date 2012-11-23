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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rtext.lang.util.Exceptions;


public class DefaultConnectorProvider implements ConnectorProvider{

	private final ConnectorConfigProvider configFileProvider;
	private final ConnectorFactory connectorProvider;
	private final Map<String, Connector> connectors = new HashMap<String, Connector>();
	
	public DefaultConnectorProvider(ConnectorConfigProvider configFileProvider,
			ConnectorFactory connectorProvider) {
		this.configFileProvider = configFileProvider;
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
			connector.dispose();
		}
	}

	public Connector get(String modelFilePath) {
		ConnectorConfig connectorConfig = configFileProvider.get(modelFilePath);
		if(connectorConfig == null){
			return null;
		}
		return createConnector(connectorConfig);
	}

	public void dispose(String rtextFilePath) {
		Connector connector = connectors.get(getKey(new File(rtextFilePath)));
		if(connector == null){
			return;
		}
		connector.dispose();
	}

	public List<Connector> get(RTextFile rTextFile) {
		List<Connector> result = new ArrayList<Connector>();
		for (ConnectorConfig config : rTextFile.getConfigurations()) {
			result.add(createConnector(config));
		}
		return result;
	}
}