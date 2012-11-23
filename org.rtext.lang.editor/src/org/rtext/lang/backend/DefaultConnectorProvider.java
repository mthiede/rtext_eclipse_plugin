/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.util.ArrayList;
import java.util.List;


public class DefaultConnectorProvider implements ConnectorProvider{

	private final ConnectorConfigProvider configFileProvider;
	private final ConnectorFactory connectorProvider;
	private final List<Connector> connectors = new ArrayList<Connector>();
	
	public DefaultConnectorProvider(ConnectorConfigProvider configFileProvider,
			ConnectorFactory connectorProvider) {
		this.configFileProvider = configFileProvider;
		this.connectorProvider = connectorProvider;
	}
	
	protected Connector createConnector(ConnectorConfig input) {
		Connector connector = connectorProvider.createConnector(input);
		connectors.add(connector);
		return connector;
	}

	public void dispose() {
		for (Connector connector : connectors) {
			connector.dispose();
		}
	}

	public Connector get(String modelFilePath) {
		ConnectorConfig connectorConfig = configFileProvider.get(modelFilePath);
		return createConnector(connectorConfig);
	}

}