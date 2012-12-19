package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.runner.Contains;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.BackendStarter;
import org.rtext.lang.backend.Connection;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.ConnectorBusySpec;
import org.rtext.lang.specs.unit.backend.ConnectorConnectedSpec;
import org.rtext.lang.specs.unit.backend.ConnectorDisconnectSpec;
import org.rtext.lang.specs.unit.backend.ConnectorErrorHandlingSpec;
import org.rtext.lang.specs.unit.backend.ConnectorExecuteCommandSpec;
import org.rtext.lang.specs.unit.backend.ConnectorListenerSpec;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.MockInjector;

@Contains({ ConnectorErrorHandlingSpec.class, ConnectorExecuteCommandSpec.class, ConnectorBusySpec.class, ConnectorConnectedSpec.class, ConnectorDisconnectSpec.class, ConnectorListenerSpec.class })
@SuppressWarnings("all")
@Named("Connector")
@RunWith(ExampleGroupRunner.class)
@CreateWith(value = MockInjector.class)
public class ConnectorSpec {
  public Connector subject;
  
  @Rule
  @Extension
  public TemporaryFolder _temporaryFolder = new Function0<TemporaryFolder>() {
    public TemporaryFolder apply() {
      TemporaryFolder _temporaryFolder = new TemporaryFolder();
      return _temporaryFolder;
    }
  }.apply();
  
  @Mock
  BackendStarter processRunner;
  
  @Mock
  Connection connection;
  
  @Mock
  Callback<Response> callback;
  
  @Mock
  Callback<LoadedModel> loadedModelCallback;
  
  final int PORT = 1234;
  
  final String ADDRESS = "127.0.0.1";
  
  final Command<Response> anyCommand = Commands.ANY_COMMAND;
  
  final Command<Response> otherCommand = Commands.OTHER_COMMAND;
  
  final String COMMAND = "cmd";
  
  File executionDir;
  
  ConnectorConfig config;
  
  @Before
  public void before() throws Exception {
    File _root = this._temporaryFolder.getRoot();
    this.executionDir = _root;
    ConnectorConfig _connectorConfig = new ConnectorConfig(this.executionDir, this.COMMAND, "*.*");
    this.config = _connectorConfig;
    Connector _connector = new Connector(this.processRunner, this.connection, this.loadedModelCallback);
    this.subject = _connector;
    int _port = this.processRunner.getPort();
    OngoingStubbing<Integer> _when = Mockito.<Integer>when(Integer.valueOf(_port));
    _when.thenReturn(Integer.valueOf(this.PORT));
  }
}
