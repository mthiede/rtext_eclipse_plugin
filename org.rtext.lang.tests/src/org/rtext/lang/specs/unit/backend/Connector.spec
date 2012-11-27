package org.rtext.lang.specs.unit.backend

import java.io.File
import org.jnario.runner.CreateWith
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.mockito.Mock
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.BackendStarter
import org.rtext.lang.commands.Callback
import org.rtext.lang.backend.Connection
import org.rtext.lang.backend.Connector
import org.rtext.lang.commands.LoadModelCommand
import org.rtext.lang.specs.util.MockInjector
import org.rtext.lang.commands.Response
import org.rtext.lang.commands.LoadedModel
import org.rtext.lang.commands.Command
import org.rtext.lang.commands.LoadModelCallback

import static org.mockito.Matchers.*
import static org.mockito.Mockito.*
import static org.rtext.lang.specs.util.Commands.*

@CreateWith(typeof(MockInjector))
describe Connector {
	
	@Rule extension TemporaryFolder = new TemporaryFolder
	@Mock BackendStarter processRunner
	@Mock Connection connection
	@Mock Callback<Response> callback
	@Mock Callback<LoadedModel> loadedModelCallback

	val PORT = 1234
	val anyCommand = ANY_COMMAND
	val otherCommand = OTHER_COMMAND
	val COMMAND = "cmd"
	File executionDir

	ConnectorConfig config
	
	before {
		executionDir = root
		config = new ConnectorConfig(executionDir, COMMAND, "*.*")
		subject = new Connector(config, processRunner, connection, loadedModelCallback)
		when(processRunner.getPort).thenReturn(PORT)
	}
	
	context "Error Handling"{
		fact "Kills backend process if connection fails"{
			doThrow(new RuntimeException()).when(connection).connect(anyString, anyInt) 
			subject.execute(anyCommand, callback)
			verify(processRunner).stop
		}
		
		fact "Closes tcp client if connection fails"{
			doThrow(new RuntimeException()).when(connection).connect(anyString, anyInt) 
			subject.execute(anyCommand, callback) 
			verify(connection).close
		}
		
		fact "Kills backend process if sending command fails"{
			doThrow(new RuntimeException()).when(connection).sendRequest(<Command>any, <Callback>any) 
			subject.execute(anyCommand, callback) throws RuntimeException
			verify(processRunner).stop
		}
		
		fact "Closes tcp client if sending command fails"{
			doThrow(new RuntimeException()).when(connection).sendRequest(<Command>any, <Callback>any) 
			subject.execute(anyCommand, callback) throws RuntimeException
			verify(connection).close
		}
		
		fact "Kills backend process & tcp client if an error occurs"{
			doAnswer[
				val callback = getArguments().get(1) as Callback
				callback.handleError("something happend")
				return null
      			].when(connection).sendRequest(<Command>any, <Callback>any)
			subject.execute(anyCommand, callback)
			verify(processRunner, times(2)).stop
			verify(connection, times(2)).close
		}
	}
	
	context "Execute command"{
		
		fact "Starts backend process"{
			subject.execute(anyCommand, callback)
			verify(processRunner).startProcess(config)
		}
		
		fact "Backend process is started only once"{
			subject.execute(anyCommand, callback)
			when(processRunner.isRunning).thenReturn(true)
			
			subject.execute(anyCommand, callback)
			verify(processRunner, times(1)).startProcess(config)
		}
		
		fact "Connects to 127.0.0.1 on specified port"{
			subject.execute(anyCommand, callback)
			verify(connection).connect("127.0.0.1", PORT)
		}
		
		fact "Notifies callback"{
			doThrow(new RuntimeException()).when(connection).connect(anyString, anyInt) 
			subject.execute(anyCommand, callback)
			verify(callback).handleError("Could not connect to backend")
			verify(connection, never).sendRequest(eq(anyCommand), any)
		}
		
		fact "Sends command's request via connection"{
			subject.execute(anyCommand, callback)
			verify(connection).sendRequest(eq(anyCommand), any)
		}
		
		fact "Loads model after connection"{
			subject.execute(anyCommand, callback)
			when(processRunner.running).thenReturn(true)
			subject.execute(otherCommand, callback)
			inOrder(connection) => [
				it.verify(connection).sendRequest(isA(typeof(LoadModelCommand)), any)
				it.verify(connection).sendRequest(eq(anyCommand), any)
				it.verify(connection).sendRequest(eq(otherCommand), any)
			]
		}
		
		fact "Loads model after connection only if command is not load model"{
			when(processRunner.running).thenReturn(false, true)
			subject.execute(new LoadModelCommand, LoadModelCallback::create(config))
			verify(connection).sendRequest(isA(typeof(LoadModelCommand)), any)
		}
		
		fact "Returns commands response"{
			subject.execute(anyCommand, callback)
			verify(connection).sendRequest(eq(anyCommand), any)
		}
	}
	
	context "Connected"{
		fact "initially disconnected"{
			subject.connected => false
		}
		
		fact "connected if process is running"{
			when(processRunner.running).thenReturn(true)
			subject.connected => true
		}
		
		fact "disconnected if process is not running"{
			when(processRunner.running).thenReturn(false)
			subject.connected => false
		}
	}
	
	context disconnect{
		
		fact "closes connection"{
			subject.disconnect
			verify(connection).close
		}
		
		fact "stops process runner"{
			subject.disconnect
			verify(processRunner).stop
		}
	}
	
}