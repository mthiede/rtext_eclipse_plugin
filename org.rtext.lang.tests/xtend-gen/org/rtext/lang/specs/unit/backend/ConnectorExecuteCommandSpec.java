package org.rtext.lang.specs.unit.backend;

import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
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
  @Order(6)
  public void _startsBackendProcess() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.startProcess(this.config);
  }
  
  @Test
  @Named("Backend process is started only once")
  @Order(7)
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
  @Order(8)
  public void _connectsTo127001OnSpecifiedPort() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.connect("127.0.0.1", this.PORT);
  }
  
  @Test
  @Named("Notifies callback")
  @Order(9)
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
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify_1.<Response>sendRequest(_eq, _any);
  }
  
  @Test
  @Named("Sends command\\\'s request via connection")
  @Order(10)
  public void _sendsCommandSRequestViaConnection() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify.<Response>sendRequest(_eq, _any);
  }
  
  @Test
  @Named("Loads model after connection")
  @Order(11)
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
          Callback<LoadedModel> _any = Matchers.<Callback<LoadedModel>>any();
          _verify.<LoadedModel>sendRequest(_isA, _any);
          Connection _verify_1 = it.<Connection>verify(ConnectorExecuteCommandSpec.this.connection);
          Command<Response> _eq = Matchers.<Command<Response>>eq(ConnectorExecuteCommandSpec.this.anyCommand);
          Callback<Response> _any_1 = Matchers.<Callback<Response>>any();
          _verify_1.<Response>sendRequest(_eq, _any_1);
          Connection _verify_2 = it.<Connection>verify(ConnectorExecuteCommandSpec.this.connection);
          Command<Response> _eq_1 = Matchers.<Command<Response>>eq(ConnectorExecuteCommandSpec.this.otherCommand);
          Callback<Response> _any_2 = Matchers.<Callback<Response>>any();
          _verify_2.<Response>sendRequest(_eq_1, _any_2);
        }
      };
    ObjectExtensions.<InOrder>operator_doubleArrow(_inOrder, _function);
  }
  
  @Test
  @Named("Returns commands response")
  @Order(12)
  public void _returnsCommandsResponse() throws Exception {
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    Command<Response> _eq = Matchers.<Command<Response>>eq(this.anyCommand);
    Callback<Response> _any = Matchers.<Callback<Response>>any();
    _verify.<Response>sendRequest(_eq, _any);
  }
}
