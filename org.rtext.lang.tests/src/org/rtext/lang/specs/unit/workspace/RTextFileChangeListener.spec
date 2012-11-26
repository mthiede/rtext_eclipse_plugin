package org.rtext.lang.specs.unit.workspace

import org.jnario.runner.CreateWith
import org.mockito.Mock
import org.rtext.lang.RTextPlugin
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
describe RTextFileChangeListener {
	
	extension static WorkspaceHelper = new WorkspaceHelper
	
	@Mock ConnectorProvider connectorProvider
	@Mock Connector connector
	
	before{
		RTextPlugin::getDefault.stopListeningForRTextFileChanges
		createProject("test")
		subject = new RTextFileChangeListener(connectorProvider)
		when(connectorProvider.get(any(typeof(ConnectorConfig)))).thenReturn(connector)
	}
	
	facts "stops connectors when file is changed"{
		createRTextFile
		addListener
		rtextFile.append('\n')
		waitForRTextJobs
		verify(connectorProvider).dispose(rtextFile.location.toString)
	}
	
	facts "triggers model load for each config"{
		createRTextFile
		addListener
		rtextFile.append('\n')
		waitForRTextJobs
		verify(connector, times(2)).execute(any(typeof(LoadModelCommand)), any)
	}
	
	facts "does nothing if file is created"{
		addListener
		createRTextFile
		waitForRTextJobs
		verify(connectorProvider, never).dispose(anyString)
	}
	
	facts "stops connectors when file is deleted"{
		createRTextFile
		addListener
		rtextFile.delete
		waitForRTextJobs
		verify(connectorProvider).dispose(rtextFile.location.toString)
	}
	
	after{
		workspace.removeResourceChangeListener(subject)
	}
	
	def addListener(){
		subject = new RTextFileChangeListener(connectorProvider)
		workspace.addResourceChangeListener(subject)
	}
	
	def rtextFile(){
		"test/.rtext".file
	}
	
	def createRTextFile(){
		'''
		*.ect:
		ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb "*.ect" 2>&1
		*.ect2:
		ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb "*.ect2" 2>&1
		'''.writeToFile("test/.rtext")
	}
	
	after {
		cleanUpWorkspace
		RTextPlugin::getDefault.startListeningForRTextFileChanges
		}
}