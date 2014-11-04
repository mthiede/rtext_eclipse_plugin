package org.rtext.lang.specs.unit.backend;

import org.jnario.lib.Assert;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.backend.BackendStarter;
import org.rtext.lang.backend.Connection;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@Named("Error Handling")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ConnectorErrorHandlingSpec extends ConnectorSpec {
  @Test
  @Named("Kills backend process if connection fails")
  @Order(1)
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
  @Named("Closes tcp client if connection fails")
  @Order(2)
  public void _closesTcpClientIfConnectionFails() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    String _anyString = Matchers.anyString();
    int _anyInt = Matchers.anyInt();
    _when.connect(_anyString, _anyInt);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.close();
  }
  
  @Test
  @Named("Kills backend process if sending command fails")
  @Order(3)
  public void _killsBackendProcessIfSendingCommandFails() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    Command _any = Matchers.<Command>any();
    Callback _any_1 = Matchers.<Callback>any();
    _when.<Response>sendRequest(_any, _any_1);
    boolean expectedException = false;
    String message = "";
    try{
      this.subject.<Response>execute(this.anyCommand, this.callback);
      message = "Expected " + RuntimeException.class.getName() + " for \n     subject.execute(anyCommand, callback)\n with:"
       + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
       + "\n     anyCommand is " + new org.hamcrest.StringDescription().appendValue(this.anyCommand).toString()
       + "\n     callback is " + new org.hamcrest.StringDescription().appendValue(this.callback).toString();
    }catch(RuntimeException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.stop();
  }
  
  @Test
  @Named("Closes tcp client if sending command fails")
  @Order(4)
  public void _closesTcpClientIfSendingCommandFails() throws Exception {
    RuntimeException _runtimeException = new RuntimeException();
    Stubber _doThrow = Mockito.doThrow(_runtimeException);
    Connection _when = _doThrow.<Connection>when(this.connection);
    Command _any = Matchers.<Command>any();
    Callback _any_1 = Matchers.<Callback>any();
    _when.<Response>sendRequest(_any, _any_1);
    boolean expectedException = false;
    String message = "";
    try{
      this.subject.<Response>execute(this.anyCommand, this.callback);
      message = "Expected " + RuntimeException.class.getName() + " for \n     subject.execute(anyCommand, callback)\n with:"
       + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
       + "\n     anyCommand is " + new org.hamcrest.StringDescription().appendValue(this.anyCommand).toString()
       + "\n     callback is " + new org.hamcrest.StringDescription().appendValue(this.callback).toString();
    }catch(RuntimeException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.close();
  }
  
  @Test
  @Named("Kills backend process & tcp client if an error occurs")
  @Order(5)
  public void _killsBackendProcessTcpClientIfAnErrorOccurs() throws Exception {
    final Answer<Object> _function = new Answer<Object>() {
      public Object answer(final InvocationOnMock it) throws Throwable {
        Object[] _arguments = it.getArguments();
        Object _get = _arguments[1];
        final Callback callback = ((Callback) _get);
        callback.handleError("something happend");
        return null;
      }
    };
    Stubber _doAnswer = Mockito.doAnswer(_function);
    Connection _when = _doAnswer.<Connection>when(this.connection);
    Command _any = Matchers.<Command>any();
    Callback _any_1 = Matchers.<Callback>any();
    _when.<Response>sendRequest(_any, _any_1);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    VerificationMode _times = Mockito.times(2);
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner, _times);
    _verify.stop();
    VerificationMode _times_1 = Mockito.times(2);
    Connection _verify_1 = Mockito.<Connection>verify(this.connection, _times_1);
    _verify_1.close();
  }
}
