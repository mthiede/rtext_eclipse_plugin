package org.rtext.lang.specs.unit.backend;

import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.backend2.BackendStarter;
import org.rtext.lang.backend2.Connection;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Execute command")
public class ConnectorExecuteCommandSpec extends ConnectorSpec {
  @Test
  @Named("Starts backend process")
  @Order(2)
  public void _startsBackendProcess() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.startProcess(this.config);
  }
  
  @Test
  @Named("Backend process is started only once")
  @Order(3)
  public void _backendProcessIsStartedOnlyOnce() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    boolean _isRunning = this.processRunner.isRunning();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isRunning));
    _when.thenReturn(Boolean.valueOf(true));
    this.subject.<Response>execute(this.anyCommand, this.callback);
    VerificationMode _times = Mockito.times(1);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner, _times);
    _verify.startProcess(this.config);
  }
  
  @Test
  @Named("Connects to 127.0.0.1 on specified port")
  @Order(4)
  public void _connectsTo127001OnSpecifiedPort() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.connect("127.0.0.1", this.PORT);
  }
  
  @Test
  @Named("Kills backend process if connection fails")
  @Order(5)
  public void _killsBackendProcessIfConnectionFails() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    String _anyString = Matchers.anyString();
    int _anyInt = Matchers.anyInt();
    _when.connect(_anyString, _anyInt);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.stop();
  }
  
  @Test
  @Named("Kills backend process if sending command fails")
  @Order(6)
  public void _killsBackendProcessIfSendingCommandFails() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    _when.<Response>sendRequest(this.anyCommand, this.callback);
    try{
      this.subject.<Response>execute(this.anyCommand, this.callback);
      Assert.fail("Expected " + RuntimeException.class.getName() + " in \n     subject.execute(anyCommand, callback)\n with:"
       + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
       + "\n     anyCommand is " + new StringDescription().appendValue(this.anyCommand).toString()
       + "\n     callback is " + new StringDescription().appendValue(this.callback).toString());
    }catch(RuntimeException e){
    }
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.stop();
  }
  
  @Test
  @Named("Notifies callback")
  @Order(7)
  public void _notifiesCallback() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    String _anyString = Matchers.anyString();
    int _anyInt = Matchers.anyInt();
    _when.connect(_anyString, _anyInt);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Callback<Response> _verify = Mockito.<Callback<Response>>verify(this.callback);
    _verify.handleError("Could not connect to backend");
    VerificationMode _never = Mockito.never();
    Connection _verify_1 = Mockito.<Connection>verify(this.connection, _never);
    _verify_1.<Response>sendRequest(this.anyCommand, this.callback);
  }
  
  @Test
  @Named("Sends command\\\'s request via connection")
  @Order(8)
  public void _sendsCommandSRequestViaConnection() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify.<Response>sendRequest(_eq, _any);
  }
  
  @Test
  @Named("Loads model after connection")
  @Order(9)
  public void _loadsModelAfterConnection() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    boolean _isRunning = this.processRunner.isRunning();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isRunning));
    _when.thenReturn(Boolean.valueOf(true));
    this.subject.<Response>execute(this.otherCommand, this.callback);
    InOrder _inOrder = Mockito.inOrder(this.connection);
    final Procedure1<InOrder> _function = new Procedure1<InOrder>() {
        public void apply(final InOrder it) {
          Connection _verify = it.<Connection>verify(ConnectorExecuteCommandSpec.this.connection);
          LoadModelCommand _isA = Matchers.<LoadModelCommand>isA(LoadModelCommand.class);
          Callback<LoadedModel> _eq = Matchers.<Callback<LoadedModel>>eq(ConnectorExecuteCommandSpec.this.loadedModelCallback);
          _verify.<LoadedModel>sendRequest(_isA, _eq);
          Connection _verify_1 = it.<Connection>verify(ConnectorExecuteCommandSpec.this.connection);
          _verify_1.<Response>sendRequest(ConnectorExecuteCommandSpec.this.anyCommand, ConnectorExecuteCommandSpec.this.callback);
          Connection _verify_2 = it.<Connection>verify(ConnectorExecuteCommandSpec.this.connection);
          _verify_2.<Response>sendRequest(ConnectorExecuteCommandSpec.this.otherCommand, ConnectorExecuteCommandSpec.this.callback);
        }
      };
    ObjectExtensions.<InOrder>operator_doubleArrow(_inOrder, _function);
  }
  
  @Test
  @Named("Returns commands response")
  @Order(10)
  public void _returnsCommandsResponse() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify.<Response>sendRequest(_eq, _any);
  }
}
