package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.DefaultConnectorProvider
import org.rtext.lang.backend.ConnectorConfigProvider
import org.mockito.Mock
import static org.mockito.Mockito.*
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.Connector
import org.rtext.lang.backend.ConnectorFactory
import org.jnario.runner.CreateWith
import org.rtext.lang.specs.util.MockInjector

@CreateWith(typeof(MockInjector))
describe DefaultConnectorProvider {
	
	@Mock ConnectorConfigProvider configFileProvider
	
	@Mock ConnectorConfig config
	@Mock Connector connector
	
	@Mock ConnectorFactory connectorFactory
	
	String aModelPath = "a path"
	String anotherModelPath = "another path"
	
	before {
		subject = new DefaultConnectorProvider(configFileProvider, connectorFactory)
		when(connectorFactory.createConnector(config)).thenReturn(connector)
		when(configFileProvider.get(anyString)).thenReturn(config)
	}
	
	fact "Creates connector with file specific configuration"{
		subject.get(aModelPath) => typeof(Connector)
		
		verify(configFileProvider).get(aModelPath)
	}
	
	fact "disposes all connectors"{
		subject.get(aModelPath)
		subject.dispose
		verify(connector).dispose
	}
}