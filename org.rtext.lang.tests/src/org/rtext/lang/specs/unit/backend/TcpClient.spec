package org.rtext.lang.specs.unit.backend

import java.util.concurrent.CountDownLatch
import org.rtext.lang.commands.Response
import org.rtext.lang.backend.TcpClient
import org.rtext.lang.specs.util.TcpTestServer
import org.rtext.lang.specs.util.TestCallBack
import org.rtext.lang.backend.BackendException
import org.mockito.Mock
import org.jnario.runner.CreateWith
import org.rtext.lang.specs.util.MockInjector
import org.rtext.lang.backend.TcpClientListener
import org.rtext.lang.commands.CommandSerializer

import static org.junit.Assert.*
import static org.rtext.lang.specs.util.Commands.*
import static org.rtext.lang.specs.util.Wait.*
import static org.mockito.Mockito.*

import static extension org.jnario.lib.Should.*

@CreateWith(typeof(MockInjector))
describe TcpClient{
	int INVALID_PORT = -1
	
	val progressMessage = '{"type":"progress","invocation_id":111,"percentage":100}'
	val responseMessage = '{"type":"response", "invocation_id":111, "problems":[], "total_problems":0}'
	
	val PORT = 12345
	val ANOTHER_PORT = PORT+1
	val ADDRESS = "127.0.0.1"
	val startSignal = new CountDownLatch(2);
	val server = new TcpTestServer(ADDRESS, PORT)
	val anotherSever = new TcpTestServer(ADDRESS, ANOTHER_PORT)
	val callback = new TestCallBack<Response>
	
	@Mock TcpClientListener listener
	
	before {
		server.start(startSignal)
		anotherSever.start(startSignal)
		println("wait for server")
		startSignal.await();
		subject = TcpClient::create(listener)
	}
	
	context "Sending requests"{
		
		fact "Connecting to the same address twice will create only one socket"{
			subject.connect(ADDRESS, PORT)
			subject.connect(ADDRESS, PORT)
			verify(listener).connected(ADDRESS, PORT)
		}
		
		fact "Connecting to a different address will close the previous connection"{
			subject.connect(ADDRESS, PORT)
			subject.connect(ADDRESS, ANOTHER_PORT)
			inOrder(listener) => [
				it.verify(listener).connected(ADDRESS, PORT)
				it.verify(listener).close
				it.verify(listener).connected(ADDRESS, ANOTHER_PORT)
			]
		}
		
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
	
	context "notifies message listener on"{
		
		fact "connect"{
			subject.connect(ADDRESS, PORT)
			verify(listener).connected(ADDRESS, PORT)
		}
		
		fact "request sent"{
			server.responses += responseMessage
			subject.connect(ADDRESS, PORT)
			subject.sendRequest(ANY_COMMAND, callback) 
			waitUntil[callback.response != null]
			verify(listener).messageSent(ANY_COMMAND_SERIALIZED)
		}
		
		fact "message received"{
			server.responses += responseMessage
			subject.connect(ADDRESS, PORT)
			subject.sendRequest(ANY_COMMAND, callback) 
			waitUntil[callback.response != null]
			verify(listener).messageReceived(responseMessage)
		}
		
		fact "disconnect"{
			subject.close()
			verify(listener).close()
		}
		
		fact "errors"{
			subject.connect(ADDRESS, PORT)
			server.shutdown
			subject.sendRequest(ANY_COMMAND, callback) 
			waitUntil[callback.error != null]
			verify(listener).receiveError(isA(typeof(Exception)))
		}
		
	}
	
	fact subject.connect(ADDRESS, INVALID_PORT) throws IllegalArgumentException
	
	after{
		server.shutdown
		anotherSever.shutdown	
	} 
	
	def is(Response actual, CharSequence expected){
		assertEquals(expected.toString, actual)
	}
}