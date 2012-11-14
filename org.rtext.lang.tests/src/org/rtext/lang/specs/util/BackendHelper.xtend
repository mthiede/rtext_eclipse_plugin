package org.rtext.lang.specs.util

import org.junit.After
import org.rtext.lang.backend2.CachingConnectorProvider
import org.rtext.lang.backend2.Connector
import org.rtext.lang.backend2.Response
import static org.rtext.lang.specs.util.Wait.*
import org.rtext.lang.backend2.Command
import org.eclipse.jface.text.IDocument

class BackendHelper {
	extension TestFileLocator = new TestFileLocator("backends/head")
	
	val connectorProvider = CachingConnectorProvider::create
	val callback = new TestCallBack<Response>
	
	@Property IDocument document
	@Property var proposalAcceptor = new TestProposalAcceptor
	
	@Property Connector connector
	@Property Response response
	
	def startBackendFor(String filePath) {
		val absolutePath = filePath.absolutPath
		connector = connectorProvider.get(absolutePath)
		document = new SimpleDocument(Files::read(absolutePath))
	}
	
	def executeSynchronousCommand() {
		response = connector.execute(new Command(1, "request", "load_model"))
	}

	def executeAsynchronousCommand() {
		waitUntil[
			connector.execute(new Command(2, "request", "load_model"), callback)
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
	

}