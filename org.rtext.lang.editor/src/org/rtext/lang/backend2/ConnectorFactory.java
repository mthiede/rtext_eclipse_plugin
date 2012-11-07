package org.rtext.lang.backend2;

import org.rtext.lang.backend.ConnectorConfig;

public class ConnectorFactory {
	
	public Connector createConnector(ConnectorConfig connectorConfig){
		return Connector.create(connectorConfig);
	}

}
