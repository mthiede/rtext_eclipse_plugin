/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import org.rtext.lang.util.Expectations;


public class ConnectorFactory {
	
	public Connector createConnector(ConnectorConfig connectorConfig){
		Expectations.expectNotNull(connectorConfig);
		return Connector.create(connectorConfig);
	}

}
