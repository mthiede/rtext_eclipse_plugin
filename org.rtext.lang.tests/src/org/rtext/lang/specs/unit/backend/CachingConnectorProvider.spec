package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.CachingConnectorProvider
import org.jnario.runner.CreateWith
import org.rtext.lang.specs.util.MockInjector
import org.mockito.Mock
import org.rtext.lang.backend.ConnectorConfigProvider
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.Connector
import org.rtext.lang.backend.ConnectorProvider

import static org.mockito.Mockito.*
import org.rtext.lang.backend.ConnectorFactory
import java.io.File

@CreateWith(typeof(MockInjector))
describe ConnectorProvider {
	
	@Mock ConnectorConfigProvider configFileProvider
	ConnectorConfig config = new ConnectorConfig(new File("test1/.rtext"), "")
	ConnectorConfig anotherConfig = new ConnectorConfig(new File("test2/.rtext"), "")
	@Mock ConnectorFactory connectorFactory
	@Mock Connector connector
	@Mock Connector anotherConnector
		
	String aModelPath = "a path"
	String anotherModelPath = "another path"
	
	before {
		subject = new CachingConnectorProvider(configFileProvider, connectorFactory)
		when(connectorFactory.createConnector(config)).thenReturn(connector)
		when(connectorFactory.createConnector(anotherConfig)).thenReturn(anotherConnector)
	}
	
	fact "Creates connector with file specific configuration"{
		when(configFileProvider.get(anyString)).thenReturn(config)
		
		subject.get(aModelPath) => typeof(Connector)
		
		verify(configFileProvider).get(aModelPath)
	}
	
	fact "returns same connector for files with same configuration"{
		when(configFileProvider.get(anyString)).thenReturn(config)
		
		subject.get(aModelPath) should be subject.get(anotherModelPath)
	}
	
	fact "returns different connector for files with different configuration"{
		when(configFileProvider.get(aModelPath)).thenReturn(config)
		when(configFileProvider.get(anotherModelPath)).thenReturn(anotherConfig)
		
		subject.get(aModelPath) should not be subject.get(anotherModelPath)
	}
	
	fact "returns null if config file cannot be resolved"{
		when(configFileProvider.get(anyString)).thenReturn(null)
		subject.get(aModelPath) should be null
	}
	
	fact "disposes all connectors for given config file"{
		when(configFileProvider.get(aModelPath)).thenReturn(config)
		subject.get(aModelPath)
		subject.dispose(config.configFile.path)
		verify(connector).dispose
	}
}