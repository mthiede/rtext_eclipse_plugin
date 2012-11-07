package org.rtext.lang.backend2;

import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.util.Cache;
import org.rtext.lang.util.Function;

public class CachingConnectorProvider extends DefaultConnectorProvider implements ConnectorProvider {

	public static ConnectorProvider create() {
		return new CachingConnectorProvider(FileSystemBasedConfigProvider.create(), new ConnectorFactory());
	}

	private final Cache<ConnectorConfig, Connector> cache = Cache.create(new Function<ConnectorConfig, Connector>() {
		public Connector apply(ConnectorConfig input) {
			return CachingConnectorProvider.super.createConnector(input);
		}

	});

	public CachingConnectorProvider(ConnectorConfigProvider configFileProvider, ConnectorFactory connectorProvider) {
		super(configFileProvider, connectorProvider);
	}
	
	@Override
	protected Connector createConnector(ConnectorConfig connectorConfig) {
		return cache.get(connectorConfig);
	}

}
