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
import org.rtext.lang.backend.FileSystemBasedConfigProvider
import org.rtext.lang.commands.LoadModelCallback
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.editor.Connected
import org.rtext.lang.RTextPlugin
import org.junit.Before

class BackendHelper implements Connected{
	ConnectorConfig config
	extension TestFileLocator fileLocator = TestFileLocator::getDefault()
	
	val connectorProvider = CachingConnectorProvider::create
	val callback = new TestCallBack<Response>
	val configProvider = FileSystemBasedConfigProvider::create()
	
	public IDocument document
	var proposalAcceptor = new TestProposalAcceptor
	
	public Connector connector
	public Response response
	
	def startBackendFor(IPath filePath) {
		startBackendFor(filePath.toOSString)
	}	
	
	def absolutPath(String relativePath){
		fileLocator.absolutPath(relativePath)
	}
	
	def startBackendFor(String filePath) {
		val absolutePath = filePath
		config = configProvider.get(absolutePath)
		connector = connectorProvider.get(config)
		document = new SimpleDocument(Files::read(absolutePath))
		config
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
	
	def loadModel(){
		val loadModelCallback = LoadModelCallback::create(config)
		val waitingCallback = new WrappingCallback<LoadedModel>(loadModelCallback)
		connector.execute(new LoadModelCommand, waitingCallback)
		waitingCallback.waitForResponse
	}
	
	@Before
	def setUp(){
		connectorProvider.dispose
		RTextPlugin::getDefault().stopListeningForRTextFileChanges
	}	
	
	@After
	def teardown(){
		connectorProvider.dispose
		RTextPlugin::getDefault().startListeningForRTextFileChanges
	}	
	
	def offsetAfter(String substring){
		document.get.indexOf(substring) + substring.length
	}

	def proposals(){
		proposalAcceptor.proposals
	}

	def regionOf(String string){
		val offset = document.get.indexOf(string)
		new Region(offset+1, string.length)
	}
	
	def busy(){
		connector.connect
	}
	
	override getConnector() {
		connectorProvider.get(config)
	}

}