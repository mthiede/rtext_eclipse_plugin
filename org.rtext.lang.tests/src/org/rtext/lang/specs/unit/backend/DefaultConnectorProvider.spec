package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.DefaultConnectorProvider
import org.rtext.lang.backend.ConnectorConfigProvider
import org.mockito.Mock
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.Connector
import org.rtext.lang.backend.ConnectorFactory
import org.jnario.runner.CreateWith
import org.rtext.lang.specs.util.MockInjector
import java.io.File

import static org.mockito.Mockito.*

@CreateWith(typeof(MockInjector))
describe DefaultConnectorProvider {
	
	@Mock ConnectorConfigProvider configFileProvider
	
	ConnectorConfig config = new ConnectorConfig(new File("path"), "")
	@Mock Connector connector
	
	@Mock ConnectorFactory connectorFactory
	
	String aModelPath = "a path"
	String anotherModelPath = "another path"
	
	before {
		subject = new DefaultConnectorProvider(connectorFactory)
		when(connectorFactory.createConnector(config)).thenReturn(connector)
		when(configFileProvider.get(anyString)).thenReturn(config)
	}
	
	fact "Creates connector for configuration"{
		subject.get(config) => typeof(Connector)
		
		verify(connectorFactory).createConnector(config)
	}
	
	fact "disposes all connectors"{
		subject.get(config)
		subject.dispose
		verify(connector).disconnect
	}
}