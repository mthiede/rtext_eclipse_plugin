/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import org.rtext.lang.util.Cache;
import org.rtext.lang.util.Function;

public class CachingConnectorProvider extends DefaultConnectorProvider implements ConnectorProvider {

	public static ConnectorProvider create() {
		return new CachingConnectorProvider(new ConnectorFactory());
	}

	private final Cache<ConnectorConfig, Connector> cache = Cache.create(new Function<ConnectorConfig, Connector>() {
		public Connector apply(ConnectorConfig input) {
			return CachingConnectorProvider.super.createConnector(input);
		}

	});

	public CachingConnectorProvider(ConnectorFactory connectorProvider) {
		super(connectorProvider);
	}
	
	@Override
	protected Connector createConnector(ConnectorConfig connectorConfig) {
		return cache.get(connectorConfig);
	}

}
