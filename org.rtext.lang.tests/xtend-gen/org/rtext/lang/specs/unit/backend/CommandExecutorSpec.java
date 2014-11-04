package org.rtext.lang.specs.unit.backend;

import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.CommandExecutor;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.commands.Response;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.workspace.BackendConnectJob;

@CreateWith(MockInjector.class)
@Named("CommandExecutor")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class CommandExecutorSpec {
  public CommandExecutor subject;
  
  @Mock
  CommandExecutor.ExecutionHandler<Response> handler;
  
  @Mock
  BackendConnectJob connectJob;
  
  @Mock
  BackendConnectJob.Factory connectJobFactory;
  
  @Mock
  Connected connected;
  
  @Mock
  Connector connector;
  
  @Before
  public void before() throws Exception {
    Connector _connector = this.connected.getConnector();
    OngoingStubbing<Connector> _when = Mockito.<Connector>when(_connector);
    _when.thenReturn(this.connector);
    Connector _any = Matchers.<Connector>any();
    BackendConnectJob _create = this.connectJobFactory.create(_any);
    OngoingStubbing<BackendConnectJob> _when_1 = Mockito.<BackendConnectJob>when(_create);
    _when_1.thenReturn(this.connectJob);
    CommandExecutor _commandExecutor = new CommandExecutor(this.connected, this.connectJobFactory);
    this.subject = _commandExecutor;
  }
  
  @Test
  @Named("provides error if no connector")
  @Order(1)
  public void _providesErrorIfNoConnector() throws Exception {
    Connector _connector = this.connected.getConnector();
    OngoingStubbing<Connector> _when = Mockito.<Connector>when(_connector);
    _when.thenReturn(null);
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    CommandExecutor.ExecutionHandler<Response> _verify = Mockito.<CommandExecutor.ExecutionHandler<Response>>verify(this.handler);
    _verify.handle(CommandExecutor.BACKEND_NOT_FOUND);
    Mockito.verifyNoMoreInteractions(this.handler);
  }
  
  @Test
  @Named("provides error if not connected")
  @Order(2)
  public void _providesErrorIfNotConnected() throws Exception {
    boolean _isConnected = this.connector.isConnected();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isConnected));
    _when.thenReturn(Boolean.valueOf(false));
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    CommandExecutor.ExecutionHandler<Response> _verify = Mockito.<CommandExecutor.ExecutionHandler<Response>>verify(this.handler);
    _verify.handle(CommandExecutor.MODEL_NOT_YET_LOADED);
    Mockito.verifyNoMoreInteractions(this.handler);
  }
  
  @Test
  @Named("start backend if not connected")
  @Order(3)
  public void _startBackendIfNotConnected() throws Exception {
    boolean _isConnected = this.connector.isConnected();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isConnected));
    _when.thenReturn(Boolean.valueOf(false));
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    BackendConnectJob _verify = Mockito.<BackendConnectJob>verify(this.connectJob);
    _verify.execute();
  }
  
  @Test
  @Named("starts backend only once if job is already running")
  @Order(4)
  public void _startsBackendOnlyOnceIfJobIsAlreadyRunning() throws Exception {
    boolean _isConnected = this.connector.isConnected();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isConnected));
    _when.thenReturn(Boolean.valueOf(false));
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    Boolean _isRunning = this.connectJob.isRunning();
    OngoingStubbing<Boolean> _when_1 = Mockito.<Boolean>when(_isRunning);
    _when_1.thenReturn(Boolean.valueOf(true));
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    BackendConnectJob _verify = Mockito.<BackendConnectJob>verify(this.connectJob);
    _verify.execute();
  }
  
  @Test
  @Named("provides error if busy")
  @Order(5)
  public void _providesErrorIfBusy() throws Exception {
    boolean _isConnected = this.connector.isConnected();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isConnected));
    _when.thenReturn(Boolean.valueOf(true));
    boolean _isBusy = this.connector.isBusy();
    OngoingStubbing<Boolean> _when_1 = Mockito.<Boolean>when(Boolean.valueOf(_isBusy));
    _when_1.thenReturn(Boolean.valueOf(true));
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    CommandExecutor.ExecutionHandler<Response> _verify = Mockito.<CommandExecutor.ExecutionHandler<Response>>verify(this.handler);
    _verify.handle(CommandExecutor.LOADING_MODEL);
    Mockito.verifyNoMoreInteractions(this.handler);
  }
  
  @Test
  @Named("provides response")
  @Order(6)
  public void _providesResponse() throws Exception {
    boolean _isConnected = this.connector.isConnected();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isConnected));
    _when.thenReturn(Boolean.valueOf(true));
    boolean _isBusy = this.connector.isBusy();
    OngoingStubbing<Boolean> _when_1 = Mockito.<Boolean>when(Boolean.valueOf(_isBusy));
    _when_1.thenReturn(Boolean.valueOf(false));
    Response _execute = this.connector.<Response>execute(Commands.ANY_COMMAND);
    OngoingStubbing<Response> _when_2 = Mockito.<Response>when(_execute);
    _when_2.thenReturn(Commands.ANY_COMMAND_RESPONSE);
    this.subject.<Response>run(Commands.ANY_COMMAND, this.handler);
    CommandExecutor.ExecutionHandler<Response> _verify = Mockito.<CommandExecutor.ExecutionHandler<Response>>verify(this.handler);
    _verify.handleResult(Commands.ANY_COMMAND_RESPONSE);
    Mockito.verifyNoMoreInteractions(this.handler);
  }
}
