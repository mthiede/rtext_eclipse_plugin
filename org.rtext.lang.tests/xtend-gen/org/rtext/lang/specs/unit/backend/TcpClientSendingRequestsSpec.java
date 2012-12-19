package org.rtext.lang.specs.unit.backend;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.rtext.lang.backend.BackendException;
import org.rtext.lang.backend.TcpClientListener;
import org.rtext.lang.commands.Progress;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.TcpClientSpec;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

@SuppressWarnings("all")
@Named("Sending requests")
@RunWith(ExampleGroupRunner.class)
public class TcpClientSendingRequestsSpec extends TcpClientSpec {
  @Test
  @Named("Connecting to the same address twice will create only one socket")
  @Order(2)
  public void _connectingToTheSameAddressTwiceWillCreateOnlyOneSocket() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.connect(this.ADDRESS, this.PORT);
    TcpClientListener _verify = Mockito.<TcpClientListener>verify(this.listener);
    _verify.connected(this.ADDRESS, this.PORT);
  }
  
  @Test
  @Named("Connecting to a different address will close the previous connection")
  @Order(3)
  public void _connectingToADifferentAddressWillCloseThePreviousConnection() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.connect(this.ADDRESS, this.ANOTHER_PORT);
    InOrder _inOrder = Mockito.inOrder(this.listener);
    final Procedure1<InOrder> _function = new Procedure1<InOrder>() {
        public void apply(final InOrder it) {
          TcpClientListener _verify = it.<TcpClientListener>verify(TcpClientSendingRequestsSpec.this.listener);
          _verify.connected(TcpClientSendingRequestsSpec.this.ADDRESS, TcpClientSendingRequestsSpec.this.PORT);
          TcpClientListener _verify_1 = it.<TcpClientListener>verify(TcpClientSendingRequestsSpec.this.listener);
          _verify_1.close();
          TcpClientListener _verify_2 = it.<TcpClientListener>verify(TcpClientSendingRequestsSpec.this.listener);
          _verify_2.connected(TcpClientSendingRequestsSpec.this.ADDRESS, TcpClientSendingRequestsSpec.this.ANOTHER_PORT);
        }
      };
    ObjectExtensions.<InOrder>operator_doubleArrow(_inOrder, _function);
  }
  
  @Test
  @Named("Receives responses from server")
  @Order(4)
  public void _receivesResponsesFromServer() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          Response _response = TcpClientSendingRequestsSpec.this.callback.getResponse();
          boolean _notEquals = (!Objects.equal(_response, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    Response _response = this.callback.getResponse();
    final Procedure1<Response> _function_1 = new Procedure1<Response>() {
        public void apply(final Response it) {
          String _type = it.getType();
          boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
          Assert.assertTrue("\nExpected type => \"response\" but"
           + "\n     type is " + new StringDescription().appendValue(_type).toString() + "\n", _doubleArrow);
          
        }
      };
    ObjectExtensions.<Response>operator_doubleArrow(_response, _function_1);
  }
  
  @Test
  @Named("Notifies callback when command is sent")
  @Order(5)
  public void _notifiesCallbackWhenCommandIsSent() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          Response _response = TcpClientSendingRequestsSpec.this.callback.getResponse();
          boolean _notEquals = (!Objects.equal(_response, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    boolean _hasStarted = this.callback.hasStarted();
    Assert.assertTrue("\nExpected callback.hasStarted but"
     + "\n     callback is " + new StringDescription().appendValue(this.callback).toString() + "\n", _hasStarted);
    
  }
  
  @Test
  @Named("Receives progress from server")
  @Order(6)
  public void _receivesProgressFromServer() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.progressMessage);
    ArrayList<String> _responses_1 = this.server.getResponses();
    _responses_1.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          Response _response = TcpClientSendingRequestsSpec.this.callback.getResponse();
          boolean _notEquals = (!Objects.equal(_response, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    List<Progress> _progress = this.callback.getProgress();
    int _size = _progress.size();
    boolean _doubleArrow = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(1));
    Assert.assertTrue("\nExpected callback.progress.size => 1 but"
     + "\n     callback.progress.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     callback.progress is " + new StringDescription().appendValue(_progress).toString()
     + "\n     callback is " + new StringDescription().appendValue(this.callback).toString() + "\n", _doubleArrow);
    
    Response _response = this.callback.getResponse();
    final Procedure1<Response> _function_1 = new Procedure1<Response>() {
        public void apply(final Response it) {
          String _type = it.getType();
          boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
          Assert.assertTrue("\nExpected type => \"response\" but"
           + "\n     type is " + new StringDescription().appendValue(_type).toString() + "\n", _doubleArrow);
          
        }
      };
    ObjectExtensions.<Response>operator_doubleArrow(_response, _function_1);
  }
  
  @Test
  @Named("Throws exception if not connected")
  @Order(7)
  public void _throwsExceptionIfNotConnected() throws Exception {
    boolean expectedException = false;
    String message = "";
    try{
      this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
      message = "Expected " + BackendException.class.getName() + " for \n     subject.sendRequest(ANY_COMMAND, callback)\n with:"
       + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
       + "\n     ANY_COMMAND is " + new StringDescription().appendValue(Commands.ANY_COMMAND).toString()
       + "\n     callback is " + new StringDescription().appendValue(this.callback).toString();
    }catch(BackendException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
  }
}
