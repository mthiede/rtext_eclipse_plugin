package org.rtext.lang.specs.util

import org.rtext.lang.backend.ConnectorListener

@Data
class RecordingConnectorListener implements ConnectorListener {
	
	val log = <String>newArrayList
	
	override connect(String address, int port) {
		log += "connect " + address + ":"  + port
	}
	
	override disconnect() {
		log += "disconnect"
	}
	
	override executeCommand(String command) {
		log += command
	}
	
	def recorded(String message){
		log.contains(message)
	}
	
}