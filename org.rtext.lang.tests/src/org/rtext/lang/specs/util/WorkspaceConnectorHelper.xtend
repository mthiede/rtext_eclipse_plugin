package org.rtext.lang.specs.util

import java.util.List
import org.junit.After
import org.rtext.lang.RTextPlugin
import org.rtext.lang.backend.Connector
import org.rtext.lang.backend.FileSystemBasedConfigProvider

import static org.jnario.lib.Wait.*
import org.rtext.lang.commands.LoadModelCommand
import org.rtext.lang.commands.Response
import org.rtext.lang.commands.Command

class WorkspaceConnectorHelper extends WorkspaceHelper{
	
	extension TestFileLocator = TestFileLocator::getDefault()

	val connectors = <String, Connector>newHashMap
	val listeners = <Connector, RecordingConnectorListener>newHashMap
	
	val connectorProvider = RTextPlugin::getDefault.connectorProvider
	
	def connector(String path){
		val config = FileSystemBasedConfigProvider::create.get(path.file.location.toString)
		
		val connector = connectorProvider.get(config)
		connectors.put(path, connector)
		
		val listener = new RecordingConnectorListener
		connector.addListener(listener)
		listeners.put(connector, listener)
		connector
	}
	
	def connect(String path){
		val connector  = connectors.get(path)
		connector.connect
		waitUntil[connector.connected]
		connector
	}
	
	def executeSynchronousCommand(String path) {
		path.connector.execute(new LoadModelCommand)
	}
	
	def executeAsynchronousCommand(String path) {
		val callback = new TestCallBack<Response>
		waitUntil[
			path.connector.execute(new Command("load_model", typeof(Response)), callback)
			callback.response != null
		]
		callback.response
	}
	
	def createRTextFile(String path, List<String> patterns){
		createFile(path, '''
			«FOR ext : patterns»
			«ext»:
			ruby -I «root»/rgen/lib -I «root»/rtext/lib «root»/rtext/test/integration/ecore_editor.rb "«ext»" 2>&1
			«ENDFOR»
		''') 
	}
	
	@After override cleanUpWorkspace(){
		connectors.values.forEach[disconnect]
		super.cleanUpWorkspace
	}
	
	def log(String path){
		val connector = connectors.get(path)
		val listener = listeners.get(connector)
		listener.log.join("\n")
	}
}