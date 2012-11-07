package org.rtext.lang.specs.integration

import org.rtext.lang.backend2.CachingConnectorProvider
import org.rtext.lang.backend2.Command
import org.rtext.lang.backend2.Connector
import org.rtext.lang.backend2.Response
import org.rtext.lang.specs.util.TestFileLocator
import org.rtext.lang.specs.util.TestCallBack

import static org.rtext.lang.backend2.Response.*
import static org.rtext.lang.specs.util.Wait.*

import static extension org.jnario.lib.Should.*

describe "Communication with Backend"{
	
	extension TestFileLocator = new TestFileLocator("backends/head")
	val modelFile = "rtext/test/integration/model/test_metamodel.ect".absolutPath
		
	val connectorProvider = CachingConnectorProvider::create
	val callback = new TestCallBack
	Connector connector
	Response response
	
	fact "Executing commands synchronously"{
		startBackendFor(modelFile)
		
		executeSynchronousCommand() 
		
		response.type => "response"
	}
	
	fact "Executing commands asynchronously"{
		startBackendFor(modelFile)
		
		executeAsynchronousCommand() 
		
		response.type => "response"
	}
	
	after connectorProvider.dispose
		
	def startBackendFor(String filePath) {
		connector = connectorProvider.get(filePath)
	}
	
	def executeSynchronousCommand() {
		response = connector.execute(new Command(1, "request", "load_model"))
	}

	def executeAsynchronousCommand() {
		waitUntil[
			connector.execute(new Command(2, "request", "load_model"), callback)
			callback.response != null
		]
		response = callback.response
	}
}

