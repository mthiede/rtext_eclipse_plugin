package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.BackendHelper

import static extension org.jnario.lib.Should.*

describe "Communication with Backend"{
	
	extension BackendHelper = new BackendHelper
	val modelFile = "rtext/test/integration/model/test_metamodel.ect"
		
	fact "Executing commands synchronously"{
		startBackendFor(modelFile.absolutPath)
		
		executeSynchronousCommand() 
		
		response.type => "response"
	}
	
	fact "Executing commands asynchronously"{
		startBackendFor(modelFile.absolutPath)
		
		executeAsynchronousCommand() 
		
		response.type => "response"
	}
}

