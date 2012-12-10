package org.rtext.lang.specs.unit.workspace

import org.jnario.runner.CreateWith
import org.junit.Ignore
import org.mockito.Mock
import org.rtext.lang.backend.Connector
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.ConnectorProvider
import org.rtext.lang.commands.LoadModelCommand
import org.rtext.lang.specs.util.MockInjector
import org.rtext.lang.specs.util.WorkspaceHelper
import org.rtext.lang.workspace.RTextFileChangeListener

import static org.mockito.Matchers.*
import static org.mockito.Mockito.*
import static org.rtext.lang.specs.util.Jobs.*

import static extension org.rtext.lang.specs.unit.workspace.RTextFileChangeListenerSpec.*

@CreateWith(typeof(MockInjector))
@Ignore
describe RTextFileChangeListener {
	
	extension static WorkspaceHelper = new WorkspaceHelper
	
	@Mock ConnectorProvider connectorProvider
	@Mock Connector connector
	
	before{
		createProject("rtext_test2")
		subject = new RTextFileChangeListener(connectorProvider)
		when(connectorProvider.get(any(typeof(ConnectorConfig)))).thenReturn(connector)
	}
	
	pending fact "stops connectors when rtext file is changed"{
		createRTextFile
		addListener
		rtextFile.append('\n')
		waitForRTextJobs
		verify(connector, times(2)).disconnect()
	}
	
	pending fact "reconnects each config"{
		createRTextFile
		addListener
		rtextFile.append('\n')
		waitForRTextJobs
		verify(connector, times(2)).execute(isA(typeof(LoadModelCommand)), any)
	}
	
	pending fact "does nothing if file is created"{
		addListener
		createRTextFile
		waitForRTextJobs
		verify(connectorProvider, never).dispose(anyString)
	}
	
	def addListener(){
		subject = new RTextFileChangeListener(connectorProvider)
		workspace.addResourceChangeListener(subject)
	}
	
	def rtextFile(){
		"rtext_test2/.rtext".file
	}
	
	def createRTextFile(){
		'''
		*.ect:
		ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb "*.ect" 2>&1
		*.ect2:
		ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb "*.ect2" 2>&1
		'''.writeToFile("rtext_test2/.rtext")
	}
	
	after {
		cleanUpWorkspace
		workspace.removeResourceChangeListener(subject)
	}
}