package org.rtext.lang.specs.unit.backend;

import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.backend2.BackendStarter;
import org.rtext.lang.backend2.Callback;
import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.Connection;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Execute command")
public class ConnectorExecuteCommandSpec extends ConnectorSpec {
  @Test
  @Named("Starts backend process")
  @Order(1)
  public void _startsBackendProcess() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.startProcess(this.config);
  }
  
  @Test
  @Named("Backend process is started only once")
  @Order(2)
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
  @Order(3)
  public void _connectsTo127001OnSpecifiedPort() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.connect("127.0.0.1", this.PORT);
  }
  
  @Test
  @Named("Kills backend process if connection fails")
  @Order(4)
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
  @Named("Notifies callback")
  @Order(5)
  public void _notifiesCallback() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    String _anyString = Matchers.anyString();
    int _anyInt = Matchers.anyInt();
    _when.connect(_anyString, _anyInt);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Callback _verify = Mockito.<Callback>verify(this.callback);
    _verify.handleError("Could not connect to backend");
    VerificationMode _never = Mockito.never();
    Connection _verify_1 = Mockito.<Connection>verify(this.connection, _never);
    _verify_1.<Response>sendRequest(this.anyCommand, this.callback);
  }
  
  @Test
  @Named("Sends command\\\'s request via connection")
  @Order(6)
  public void _sendsCommandSRequestViaConnection() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify.<Response>sendRequest(_eq, _any);
  }
  
  @Test
  @Named("Returns commands response")
  @Order(7)
  public void _returnsCommandsResponse() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify.<Response>sendRequest(_eq, _any);
  }
}
