package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.runner.Contains;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend2.BackendStarter;
import org.rtext.lang.backend2.Callback;
import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.Connection;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.LoadedModel;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.specs.unit.backend.ConnectorExecuteCommandSpec;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.MockInjector;

@Contains(ConnectorExecuteCommandSpec.class)
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Connector")
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
    Connector _connector = new Connector(this.config, this.processRunner, this.connection, this.loadedModelCallback);
    this.subject = _connector;
    int _port = this.processRunner.getPort();
    OngoingStubbing<Integer> _when = Mockito.<Integer>when(Integer.valueOf(_port));
    _when.thenReturn(Integer.valueOf(this.PORT));
  }
  
  @Test
  @Named("Disposes connection")
  @Order(1)
  public void _disposesConnection() throws Exception {
    this.subject.dispose();
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.close();
  }
}
