package org.rtext.lang.specs.util

import org.eclipse.core.runtime.IPath
import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.Region
import org.eclipse.xtend.lib.Property
import org.junit.After
import org.rtext.lang.backend.CachingConnectorProvider
import org.rtext.lang.backend.Connector
import org.rtext.lang.commands.Callback
import org.rtext.lang.commands.Command
import org.rtext.lang.commands.LoadModelCommand
import org.rtext.lang.commands.LoadedModel
import org.rtext.lang.commands.Response

import static org.rtext.lang.specs.util.Wait.*

class BackendHelper {
	extension TestFileLocator fileLocator = TestFileLocator::getDefault()
	
	val connectorProvider = CachingConnectorProvider::create
	val callback = new TestCallBack<Response>
	
	@Property IDocument document
	@Property var proposalAcceptor = new TestProposalAcceptor
	
	@Property Connector connector
	@Property Response response
	
	def startBackendFor(IPath filePath) {
		startBackendFor(filePath.toOSString)
	}	
	
	def absolutPath(String relativePath){
		fileLocator.absolutPath(relativePath)
	}
	
	def startBackendFor(String filePath) {
		val absolutePath = filePath
		connector = connectorProvider.get(absolutePath)
		document = new SimpleDocument(Files::read(absolutePath))
	}
	
	def executeSynchronousCommand() {
		response = connector.execute(new LoadModelCommand)
	}
	
	def executeSynchronousCommand(Callback<LoadedModel> callback) {
		val waitingCallback = new WrappingCallback(callback)
		connector.execute(new LoadModelCommand, waitingCallback)
		waitingCallback.waitForResponse
	}

	def executeAsynchronousCommand() {
		waitUntil[
			connector.execute(new Command("load_model", typeof(Response)), callback)
			callback.response != null
		]
		response = callback.response
	}
	
	@After
	def teardown(){
		connectorProvider.dispose
	}	
	
	def offsetAfter(String substring){
		document.get.indexOf(substring) + substring.length + 1
	}

	def proposals(){
		proposalAcceptor.proposals
	}

	def regionOf(String string){
		val offset = document.get.indexOf(string)
		new Region(offset+1, string.length)
	}
}