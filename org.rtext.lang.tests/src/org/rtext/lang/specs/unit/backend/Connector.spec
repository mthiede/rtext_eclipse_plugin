package org.rtext.lang.specs.unit.backend

import java.io.File
import org.jnario.runner.CreateWith
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.mockito.Mock
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend2.BackendStarter
import org.rtext.lang.backend2.Callback
import org.rtext.lang.backend2.Command
import org.rtext.lang.backend2.Connection
import org.rtext.lang.backend2.Connector
import org.rtext.lang.specs.util.MockInjector

import static org.mockito.Matchers.*
import static org.mockito.Mockito.*
import static org.rtext.lang.specs.util.Commands.*

@CreateWith(typeof(MockInjector))
describe Connector {
	
	@Rule extension TemporaryFolder = new TemporaryFolder
	@Mock BackendStarter processRunner
	@Mock Connection connection
	@Mock Callback callback

	val PORT = 1234
	val anyCommand = ANY_COMMAND
	val COMMAND = "cmd"
	File executionDir

	ConnectorConfig config
	
	before {
		executionDir = root
		config = new ConnectorConfig(executionDir, COMMAND, "*.*")
		subject = new Connector(config, processRunner, connection)
		when(processRunner.getPort).thenReturn(PORT)
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
		
		fact "Sends command's request via connection"{
			subject.execute(anyCommand, callback)
			verify(connection).sendRequest(eq(anyCommand), any)
		}
		
		fact "Returns commands response"{
			subject.execute(anyCommand, callback)
			verify(connection).sendRequest(eq(anyCommand), any)
		}
	}
	
	fact "Disposes connection"{
		subject.dispose
		verify(connection).close
	}
}