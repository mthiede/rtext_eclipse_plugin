package org.rtext.lang.specs.integration

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*

Feature: Sending & Receiving Messages

	Scenario: Synchronous communication
		
		Given a backend for "rtext/test/integration/model/test_metamodel.ect"
		When I send an synchronous message
			executeSynchronousCommand() 
		Then I get a response
			response.type => "response"
			
	Scenario: Ansynchronous communication
		
		Given a backend for "rtext/test/integration/model/test_metamodel.ect"
		When I send an asynchronous message
			executeAsynchronousCommand
		Then I get a response
