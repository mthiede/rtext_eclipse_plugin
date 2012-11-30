package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.CommandExecutor
import org.jnario.runner.CreateWith
import org.rtext.lang.specs.util.MockInjector
import org.mockito.Mock
import org.rtext.lang.workspace.BackendConnectJob$Factory
import org.rtext.lang.editor.Connected
import static org.mockito.Mockito.*
import org.rtext.lang.backend.Connector
import org.rtext.lang.workspace.BackendConnectJob
import static org.rtext.lang.specs.util.Commands.*
import org.rtext.lang.backend.CommandExecutor$ExecutionHandler
import org.rtext.lang.commands.Response
import static org.rtext.lang.backend.CommandExecutor.*

@CreateWith(typeof(MockInjector))
describe CommandExecutor {
	
	@Mock ExecutionHandler<Response> handler
	@Mock BackendConnectJob connectJob
	@Mock Factory connectJobFactory
	@Mock Connected connected
	@Mock Connector connector
	
	before{
		when(connected.connector).thenReturn(connector)
		when(connectJobFactory.create(any)).thenReturn(connectJob)
		subject = new CommandExecutor(connected, connectJobFactory)
	}

	fact "provides error if no connector"{
		when(connected.connector).thenReturn(null)
		subject.run(ANY_COMMAND, handler)
		verify(handler).handle(BACKEND_NOT_FOUND)
		verifyNoMoreInteractions(handler)
	}
	
	fact "provides error if not connected"{
		when(connector.connected).thenReturn(false)
		subject.run(ANY_COMMAND, handler)
		verify(handler).handle(MODEL_NOT_YET_LOADED)
		verifyNoMoreInteractions(handler)
	}
	
	fact "start backend if not connected"{
		when(connector.connected).thenReturn(false)
		subject.run(ANY_COMMAND, handler)
		verify(connectJob).execute
	}
	
	fact "starts backend only once if job is already running"{
		when(connector.connected).thenReturn(false)
		subject.run(ANY_COMMAND, handler)
		when(connectJob.isRunning).thenReturn(true)
		subject.run(ANY_COMMAND, handler)
		verify(connectJob).execute
	}
	
	fact "provides error if busy"{
		when(connector.connected).thenReturn(true)
		when(connector.busy).thenReturn(true)
		subject.run(ANY_COMMAND, handler)
		verify(handler).handle(LOADING_MODEL)
		verifyNoMoreInteractions(handler)
	}
	
	fact "provides response"{
		when(connector.connected).thenReturn(true)
		when(connector.busy).thenReturn(false)
		when(connector.execute(ANY_COMMAND)).thenReturn(ANY_COMMAND_RESPONSE)
		subject.run(ANY_COMMAND, handler)
		verify(handler).handleResult(ANY_COMMAND_RESPONSE)
		verifyNoMoreInteractions(handler)
	}
}