package org.rtext.lang.specs.integration

import org.rtext.lang.backend2.Connector
import org.rtext.lang.backend2.Command

describe "Communication with Backend"{
	
	val connector = new Connector
	
	fact "Loads specified file"{
		connect("/model/test_metamodel.ect")
		
		val response = loadModel()
		
		response.type 			=> "response"
		response.problems.size 	=> 0	
	}
		
	
	def connect(String filePath) {
		connector.connect(filePath)
	}

	def loadModel() {
		connector.execute(new Command("load_model"))
	}
	
}

