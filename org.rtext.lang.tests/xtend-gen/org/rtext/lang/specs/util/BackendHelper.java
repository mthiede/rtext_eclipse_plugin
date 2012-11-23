package org.rtext.lang.specs.util;

import com.google.common.base.Objects;
import java.util.ArrayList;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.junit.After;
import org.rtext.lang.backend2.CachingConnectorProvider;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ConnectorProvider;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.Files;
import org.rtext.lang.specs.util.SimpleDocument;
import org.rtext.lang.specs.util.TestCallBack;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.TestProposalAcceptor;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;
import org.rtext.lang.specs.util.WrappingCallback;

@SuppressWarnings("all")
public class BackendHelper {
  private TestFileLocator fileLocator = new Function0<TestFileLocator>() {
    public TestFileLocator apply() {
      TestFileLocator _default = TestFileLocator.getDefault();
      return _default;
    }
  }.apply();
  
  private final ConnectorProvider connectorProvider = new Function0<ConnectorProvider>() {
    public ConnectorProvider apply() {
      ConnectorProvider _create = CachingConnectorProvider.create();
      return _create;
    }
  }.apply();
  
  private final TestCallBack<Response> callback = new Function0<TestCallBack<Response>>() {
    public TestCallBack<Response> apply() {
      TestCallBack<Response> _testCallBack = new TestCallBack<Response>();
      return _testCallBack;
    }
  }.apply();
  
  private IDocument _document;
  
  public IDocument getDocument() {
    return this._document;
  }
  
  public void setDocument(final IDocument document) {
    this._document = document;
  }
  
  private TestProposalAcceptor _proposalAcceptor = new Function0<TestProposalAcceptor>() {
    public TestProposalAcceptor apply() {
      TestProposalAcceptor _testProposalAcceptor = new TestProposalAcceptor();
      return _testProposalAcceptor;
    }
  }.apply();
  
  public TestProposalAcceptor getProposalAcceptor() {
    return this._proposalAcceptor;
  }
  
  public void setProposalAcceptor(final TestProposalAcceptor proposalAcceptor) {
    this._proposalAcceptor = proposalAcceptor;
  }
  
  private Connector _connector;
  
  public Connector getConnector() {
    return this._connector;
  }
  
  public void setConnector(final Connector connector) {
    this._connector = connector;
  }
  
  private Response _response;
  
  public Response getResponse() {
    return this._response;
  }
  
  public void setResponse(final Response response) {
    this._response = response;
  }
  
  public void startBackendFor(final IPath filePath) {
    String _oSString = filePath.toOSString();
    this.startBackendFor(_oSString);
  }
  
  public String absolutPath(final String relativePath) {
    String _absolutPath = this.fileLocator.absolutPath(relativePath);
    return _absolutPath;
  }
  
  public void startBackendFor(final String filePath) {
    final String absolutePath = filePath;
    Connector _get = this.connectorProvider.get(absolutePath);
    this.setConnector(_get);
    String _read = Files.read(absolutePath);
    SimpleDocument _simpleDocument = new SimpleDocument(_read);
    this.setDocument(_simpleDocument);
  }
  
  public void executeSynchronousCommand() {
    try {
      Connector _connector = this.getConnector();
      LoadModelCommand _loadModelCommand = new LoadModelCommand();
      LoadedModel _execute = _connector.<LoadedModel>execute(_loadModelCommand);
      this.setResponse(_execute);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void executeSynchronousCommand(final Callback<LoadedModel> callback) {
    try {
      WrappingCallback<LoadedModel> _wrappingCallback = new WrappingCallback<LoadedModel>(callback);
      final WrappingCallback<LoadedModel> waitingCallback = _wrappingCallback;
      Connector _connector = this.getConnector();
      LoadModelCommand _loadModelCommand = new LoadModelCommand();
      _connector.<LoadedModel>execute(_loadModelCommand, waitingCallback);
      waitingCallback.waitForResponse();
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void executeAsynchronousCommand() {
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          try {
            boolean _xblockexpression = false;
            {
              Connector _connector = BackendHelper.this.getConnector();
              Command<Response> _command = new Command<Response>(2, "request", "load_model", Response.class);
              _connector.<Response>execute(_command, BackendHelper.this.callback);
              Response _response = BackendHelper.this.callback.getResponse();
              boolean _notEquals = (!Objects.equal(_response, null));
              _xblockexpression = (_notEquals);
            }
            return Boolean.valueOf(_xblockexpression);
          } catch (Exception _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
    Wait.waitUntil(_function);
    Response _response = this.callback.getResponse();
    this.setResponse(_response);
  }
  
  @After
  public void teardown() {
    this.connectorProvider.dispose();
  }
  
  public int offsetAfter(final String substring) {
    IDocument _document = this.getDocument();
    String _get = _document.get();
    int _indexOf = _get.indexOf(substring);
    int _length = substring.length();
    int _plus = (_indexOf + _length);
    int _plus_1 = (_plus + 1);
    return _plus_1;
  }
  
  public ArrayList<String> proposals() {
    TestProposalAcceptor _proposalAcceptor = this.getProposalAcceptor();
    ArrayList<String> _proposals = _proposalAcceptor.getProposals();
    return _proposals;
  }
  
  public Region regionOf(final String string) {
    Region _xblockexpression = null;
    {
      IDocument _document = this.getDocument();
      String _get = _document.get();
      final int offset = _get.indexOf(string);
      int _plus = (offset + 1);
      int _length = string.length();
      Region _region = new Region(_plus, _length);
      _xblockexpression = (_region);
    }
    return _xblockexpression;
  }
}
