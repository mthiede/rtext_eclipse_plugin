package org.rtext.lang.specs.unit.backend

import java.io.File
import org.jnario.runner.CreateWith
import org.mockito.Mock
import org.rtext.lang.backend.CachingConnectorProvider
import org.rtext.lang.backend.Connector
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.ConnectorFactory
import org.rtext.lang.backend.ConnectorProvider
import org.rtext.lang.specs.util.MockInjector

import static org.mockito.Mockito.*

import static extension org.jnario.lib.Should.*

@CreateWith(typeof(MockInjector))
describe ConnectorProvider {
	
	ConnectorConfig config = new ConnectorConfig(new File("test1/.rtext"), "")
	ConnectorConfig anotherConfig = new ConnectorConfig(new File("test2/.rtext"), "")
	@Mock ConnectorFactory connectorFactory
	@Mock Connector connector
	@Mock Connector anotherConnector
		
	String aModelPath = "a path"
	String anotherModelPath = "another path"
	
	before {
		subject = new CachingConnectorProvider(connectorFactory)
		when(connectorFactory.createConnector(config)).thenReturn(connector)
		when(connectorFactory.createConnector(anotherConfig)).thenReturn(anotherConnector)
	}
	
	fact "Creates connector with file specific configuration"{
		subject.get(config) => typeof(Connector)
		
		verify(connectorFactory).createConnector(config)
	}
	
	fact "returns same connector for same configuration"{
		subject.get(config) should be subject.get(config)
	}
	
	fact "returns different connector for different configuration"{
		subject.get(config) should not be subject.get(anotherConfig)
	}
	
	fact "disposes all connectors for given config file"{
		subject.get(config)
		subject.dispose(config.configFile.path)
		verify(connector).disconnect
	}
}