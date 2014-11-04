package org.rtext.lang.specs.util;

import com.google.common.base.Objects;
import java.util.ArrayList;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.junit.After;
import org.junit.Before;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.CachingConnectorProvider;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.FileSystemBasedConfigProvider;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Response;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.specs.util.Files;
import org.rtext.lang.specs.util.SimpleDocument;
import org.rtext.lang.specs.util.TestCallBack;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.TestProposalAcceptor;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;
import org.rtext.lang.specs.util.WrappingCallback;

@SuppressWarnings("all")
public class BackendHelper implements Connected {
  private ConnectorConfig config;
  
  @Extension
  private TestFileLocator fileLocator = TestFileLocator.getDefault();
  
  private final ConnectorProvider connectorProvider = CachingConnectorProvider.create();
  
  private final TestCallBack<Response> callback = new TestCallBack<Response>();
  
  private final FileSystemBasedConfigProvider configProvider = FileSystemBasedConfigProvider.create();
  
  public IDocument document;
  
  private TestProposalAcceptor proposalAcceptor = new TestProposalAcceptor();
  
  public Connector connector;
  
  public Response response;
  
  public ConnectorConfig startBackendFor(final IPath filePath) {
    String _oSString = filePath.toOSString();
    return this.startBackendFor(_oSString);
  }
  
  public String absolutPath(final String relativePath) {
    return this.fileLocator.absolutPath(relativePath);
  }
  
  public ConnectorConfig startBackendFor(final String filePath) {
    ConnectorConfig _xblockexpression = null;
    {
      final String absolutePath = filePath;
      ConnectorConfig _get = this.configProvider.get(absolutePath);
      this.config = _get;
      Connector _get_1 = this.connectorProvider.get(this.config);
      this.connector = _get_1;
      String _read = Files.read(absolutePath);
      SimpleDocument _simpleDocument = new SimpleDocument(_read);
      this.document = _simpleDocument;
      _xblockexpression = this.config;
    }
    return _xblockexpression;
  }
  
  public Response executeSynchronousCommand() {
    try {
      LoadModelCommand _loadModelCommand = new LoadModelCommand();
      LoadedModel _execute = this.connector.<LoadedModel>execute(_loadModelCommand);
      return this.response = _execute;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void executeSynchronousCommand(final Callback<LoadedModel> callback) {
    final WrappingCallback<LoadedModel> waitingCallback = new WrappingCallback<LoadedModel>(callback);
    LoadModelCommand _loadModelCommand = new LoadModelCommand();
    this.connector.<LoadedModel>execute(_loadModelCommand, waitingCallback);
    waitingCallback.waitForResponse();
  }
  
  public Response executeAsynchronousCommand() {
    Response _xblockexpression = null;
    {
      final Function1<WaitConfig, Boolean> _function = new Function1<WaitConfig, Boolean>() {
        public Boolean apply(final WaitConfig it) {
          boolean _xblockexpression = false;
          {
            Command<Response> _command = new Command<Response>("load_model", Response.class);
            BackendHelper.this.connector.<Response>execute(_command, BackendHelper.this.callback);
            Response _response = BackendHelper.this.callback.getResponse();
            _xblockexpression = (!Objects.equal(_response, null));
          }
          return Boolean.valueOf(_xblockexpression);
        }
      };
      Wait.waitUntil(_function);
      Response _response = this.callback.getResponse();
      _xblockexpression = this.response = _response;
    }
    return _xblockexpression;
  }
  
  public void loadModel() {
    final LoadModelCallback loadModelCallback = LoadModelCallback.create(this.config);
    final WrappingCallback<LoadedModel> waitingCallback = new WrappingCallback<LoadedModel>(loadModelCallback);
    LoadModelCommand _loadModelCommand = new LoadModelCommand();
    this.connector.<LoadedModel>execute(_loadModelCommand, waitingCallback);
    waitingCallback.waitForResponse();
  }
  
  @Before
  public void setUp() {
    this.connectorProvider.dispose();
    RTextPlugin _default = RTextPlugin.getDefault();
    _default.stopListeningForRTextFileChanges();
  }
  
  @After
  public void teardown() {
    this.connectorProvider.dispose();
    RTextPlugin _default = RTextPlugin.getDefault();
    _default.startListeningForRTextFileChanges();
  }
  
  public int offsetAfter(final String substring) {
    String _get = this.document.get();
    int _indexOf = _get.indexOf(substring);
    int _length = substring.length();
    return (_indexOf + _length);
  }
  
  public ArrayList<String> proposals() {
    return this.proposalAcceptor.proposals;
  }
  
  public Region regionOf(final String string) {
    Region _xblockexpression = null;
    {
      String _get = this.document.get();
      final int offset = _get.indexOf(string);
      int _length = string.length();
      _xblockexpression = new Region((offset + 1), _length);
    }
    return _xblockexpression;
  }
  
  public void busy() {
    this.connector.connect();
  }
  
  public Connector getConnector() {
    return this.connectorProvider.get(this.config);
  }
}
