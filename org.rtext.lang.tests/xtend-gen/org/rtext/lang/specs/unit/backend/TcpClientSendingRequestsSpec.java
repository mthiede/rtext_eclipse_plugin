package org.rtext.lang.specs.unit.backend;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.BackendException;
import org.rtext.lang.backend2.Progress;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.specs.unit.backend.TcpClientSpec;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Sending requests")
public class TcpClientSendingRequestsSpec extends TcpClientSpec {
  @Test
  @Named("Receives responses from server")
  @Order(1)
  public void _receivesResponsesFromServer() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.sendRequest(Commands.ANY_COMMAND, this.callback);
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
  @Named("Receives progress from server")
  @Order(2)
  public void _receivesProgressFromServer() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.progressMessage);
    ArrayList<String> _responses_1 = this.server.getResponses();
    _responses_1.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.sendRequest(Commands.ANY_COMMAND, this.callback);
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
  @Order(3)
  public void _throwsExceptionIfNotConnected() throws Exception {
    try{
      this.subject.sendRequest(Commands.ANY_COMMAND, this.callback);
      Assert.fail("Expected " + BackendException.class.getName() + " in \n     subject.sendRequest(ANY_COMMAND, callback)\n with:"
       + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
       + "\n     ANY_COMMAND is " + new StringDescription().appendValue(Commands.ANY_COMMAND).toString()
       + "\n     callback is " + new StringDescription().appendValue(this.callback).toString());
    }catch(BackendException e){
    }
  }
}
