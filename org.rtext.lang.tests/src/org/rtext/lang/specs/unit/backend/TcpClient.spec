package org.rtext.lang.specs.unit.backend

import java.util.concurrent.CountDownLatch
import org.rtext.lang.backend2.Response
import org.rtext.lang.backend2.TcpClient
import org.rtext.lang.specs.util.TcpTestServer
import org.rtext.lang.specs.util.TestCallBack

import static org.junit.Assert.*
import static org.rtext.lang.specs.util.Commands.*
import static org.rtext.lang.specs.util.Wait.*

import static extension org.jnario.lib.Should.*
import org.rtext.lang.backend2.BackendException

describe TcpClient{
	int INVALID_PORT = -1
	
	val progressMessage = '{"type":"progress","invocation_id":111,"percentage":100}'
	val responseMessage = '{"type":"response", "invocation_id":111, "problems":[], "total_problems":0}'
	
	val PORT = 12345
	val ADDRESS = "127.0.0.1"
	val startSignal = new CountDownLatch(1);
	val server = new TcpTestServer(ADDRESS, PORT)
	val callback = new TestCallBack<Response>
	
	before {
		server.start(startSignal)
		println("wait for server")
		startSignal.await();
		subject = TcpClient::create
	}
	
	context "Sending requests"{
		fact "Receives responses from server"{
			server.responses += responseMessage
			subject.connect(ADDRESS, PORT)
			subject.sendRequest(ANY_COMMAND, callback)
			
			waitUntil[callback.response != null]
			callback.response => [
				type => "response"
			]
		}
		
		fact "Notifies callback when command is sent"{
			server.responses += responseMessage
			subject.connect(ADDRESS, PORT)
			subject.sendRequest(ANY_COMMAND, callback)
			waitUntil[callback.response != null]
			assert callback.hasStarted
		}
		
		fact "Receives progress from server"{
			server.responses += progressMessage
			server.responses += responseMessage
	
			subject.connect(ADDRESS, PORT)
			subject.sendRequest(ANY_COMMAND, callback)
			waitUntil[callback.response != null]
	
			callback.progress.size => 1
			callback.response => [
				type => "response"
			]
		}
		fact "Throws exception if not connected"{
			subject.sendRequest(ANY_COMMAND, callback) throws BackendException
		}
	}
	
	context "connection errors"{
		
		fact "throws exception when server does not exist"{
			subject.connect("6.6.6.6", 6666) throws BackendException
		}
		
		fact "throws exception when server is not running"{
			server.shutdown
			subject.connect(ADDRESS, PORT) throws BackendException
		}
		
		fact "callback receives error if connection is closed"{
			subject.connect(ADDRESS, PORT)
			server.shutdown
			subject.sendRequest(ANY_COMMAND, callback) 
			waitUntil[callback.error != null]
		}
	}
	
	context "connection state"{
		fact "is initially disconnected"{
			subject.isConnected => false 
		}
		
		fact "is connected after successful connection to server"{
			subject.connect(ADDRESS, PORT)
			subject.isConnected => true 
		}
		
		fact "is disconnected when server shuts down"{
			subject.connect(ADDRESS, PORT)
			server.shutdown
			subject.sendRequest(ANY_COMMAND, callback) 
			waitUntil[callback.error != null]
			subject.isConnected => false 
		}
	}
	
	fact subject.connect(ADDRESS, INVALID_PORT) throws IllegalArgumentException
	
	after server.shutdown
	
	def is(Response actual, CharSequence expected){
		assertEquals(expected.toString, actual)
	}
}