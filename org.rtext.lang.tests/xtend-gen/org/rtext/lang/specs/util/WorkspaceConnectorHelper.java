package org.rtext.lang.specs.util;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.lib.Wait;
import org.junit.After;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.FileSystemBasedConfigProvider;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.RecordingConnectorListener;
import org.rtext.lang.specs.util.TestCallBack;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.WorkspaceHelper;

@SuppressWarnings("all")
public class WorkspaceConnectorHelper extends WorkspaceHelper {
  private TestFileLocator _testFileLocator = new Function0<TestFileLocator>() {
    public TestFileLocator apply() {
      TestFileLocator _default = TestFileLocator.getDefault();
      return _default;
    }
  }.apply();
  
  private final HashMap<String,Connector> connectors = new Function0<HashMap<String,Connector>>() {
    public HashMap<String,Connector> apply() {
      HashMap<String,Connector> _newHashMap = CollectionLiterals.<String, Connector>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
  private final HashMap<Connector,RecordingConnectorListener> listeners = new Function0<HashMap<Connector,RecordingConnectorListener>>() {
    public HashMap<Connector,RecordingConnectorListener> apply() {
      HashMap<Connector,RecordingConnectorListener> _newHashMap = CollectionLiterals.<Connector, RecordingConnectorListener>newHashMap();
      return _newHashMap;
    }
  }.apply();
  
  private final ConnectorProvider connectorProvider = new Function0<ConnectorProvider>() {
    public ConnectorProvider apply() {
      RTextPlugin _default = RTextPlugin.getDefault();
      ConnectorProvider _connectorProvider = _default.getConnectorProvider();
      return _connectorProvider;
    }
  }.apply();
  
  public Connector connector(final String path) {
    Connector _xblockexpression = null;
    {
      FileSystemBasedConfigProvider _create = FileSystemBasedConfigProvider.create();
      IFile _file = this.file(path);
      IPath _location = _file.getLocation();
      String _string = _location.toString();
      final ConnectorConfig config = _create.get(_string);
      final Connector connector = this.connectorProvider.get(config);
      this.connectors.put(path, connector);
      RecordingConnectorListener _recordingConnectorListener = new RecordingConnectorListener();
      final RecordingConnectorListener listener = _recordingConnectorListener;
      connector.addListener(listener);
      this.listeners.put(connector, listener);
      _xblockexpression = (connector);
    }
    return _xblockexpression;
  }
  
  public Connector connect(final String path) {
    Connector _xblockexpression = null;
    {
      final Connector connector = this.connectors.get(path);
      connector.connect();
      final Function1<Wait,Boolean> _function = new Function1<Wait,Boolean>() {
          public Boolean apply(final Wait it) {
            boolean _isConnected = connector.isConnected();
            return Boolean.valueOf(_isConnected);
          }
        };
      Wait.waitUntil(_function);
      _xblockexpression = (connector);
    }
    return _xblockexpression;
  }
  
  public LoadedModel executeSynchronousCommand(final String path) {
    try {
      Connector _connector = this.connector(path);
      LoadModelCommand _loadModelCommand = new LoadModelCommand();
      LoadedModel _execute = _connector.<LoadedModel>execute(_loadModelCommand);
      return _execute;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Response executeAsynchronousCommand(final String path) {
    Response _xblockexpression = null;
    {
      TestCallBack<Response> _testCallBack = new TestCallBack<Response>();
      final TestCallBack<Response> callback = _testCallBack;
      final Function1<Wait,Boolean> _function = new Function1<Wait,Boolean>() {
          public Boolean apply(final Wait it) {
            boolean _xblockexpression = false;
            {
              Connector _connector = WorkspaceConnectorHelper.this.connector(path);
              Command<Response> _command = new Command<Response>("load_model", Response.class);
              _connector.<Response>execute(_command, callback);
              Response _response = callback.getResponse();
              boolean _notEquals = (!Objects.equal(_response, null));
              _xblockexpression = (_notEquals);
            }
            return Boolean.valueOf(_xblockexpression);
          }
        };
      Wait.waitUntil(_function);
      Response _response = callback.getResponse();
      _xblockexpression = (_response);
    }
    return _xblockexpression;
  }
  
  public IFile createRTextFile(final String path, final List<String> patterns) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final String ext : patterns) {
        _builder.append(ext, "");
        _builder.append(":");
        _builder.newLineIfNotEmpty();
        _builder.append("ruby -I ");
        String _root = this._testFileLocator.getRoot();
        _builder.append(_root, "");
        _builder.append("/rgen/lib -I ");
        String _root_1 = this._testFileLocator.getRoot();
        _builder.append(_root_1, "");
        _builder.append("/rtext/lib ");
        String _root_2 = this._testFileLocator.getRoot();
        _builder.append(_root_2, "");
        _builder.append("/rtext/test/integration/ecore_editor.rb \"");
        _builder.append(ext, "");
        _builder.append("\" 2>&1");
        _builder.newLineIfNotEmpty();
      }
    }
    IFile _createFile = this.createFile(path, _builder);
    return _createFile;
  }
  
  @After
  public void cleanUpWorkspace() {
    try {
      Collection<Connector> _values = this.connectors.values();
      final Procedure1<Connector> _function = new Procedure1<Connector>() {
          public void apply(final Connector it) {
            it.disconnect();
          }
        };
      IterableExtensions.<Connector>forEach(_values, _function);
      super.cleanUpWorkspace();
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String log(final String path) {
    String _xblockexpression = null;
    {
      final Connector connector = this.connectors.get(path);
      final RecordingConnectorListener listener = this.listeners.get(connector);
      ArrayList<String> _log = listener.getLog();
      String _join = IterableExtensions.join(_log, "\n");
      _xblockexpression = (_join);
    }
    return _xblockexpression;
  }
}
