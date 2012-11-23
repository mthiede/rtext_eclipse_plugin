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

@CreateWith(typeof(MockInjector))
describe ConnectorProvider {
	
	@Mock ConnectorConfigProvider configFileProvider
	@Mock ConnectorConfig config
	@Mock ConnectorConfig anotherConfig
	@Mock Connector connector
	
	String aModelPath = "a path"
	String anotherModelPath = "another path"
	
	before {
		subject = new CachingConnectorProvider(configFileProvider, new ConnectorFactory)
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
	
}