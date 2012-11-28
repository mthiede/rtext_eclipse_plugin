package org.rtext.lang.specs.unit.backend;

import java.util.ArrayList;

import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.rtext.lang.backend.TcpClientListener;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

import com.google.common.base.Objects;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("notifies message listener on")
public class TcpClientNotifiesMessageListenerOnSpec extends TcpClientSpec {
  @Test
  @Named("connect")
  @Order(14)
  public void _connect() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    TcpClientListener _verify = Mockito.<TcpClientListener>verify(this.listener);
    _verify.connected(this.ADDRESS, this.PORT);
  }
  
  @Test
  @Named("request sent")
  @Order(15)
  public void _requestSent() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          Response _response = TcpClientNotifiesMessageListenerOnSpec.this.callback.getResponse();
          boolean _notEquals = (!Objects.equal(_response, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    TcpClientListener _verify = Mockito.<TcpClientListener>verify(this.listener);
    _verify.messageSent(Commands.ANY_COMMAND_SERIALIZED);
  }
  
  @Test
  @Named("message received")
  @Order(16)
  public void _messageReceived() throws Exception {
    ArrayList<String> _responses = this.server.getResponses();
    _responses.add(this.responseMessage);
    this.subject.connect(this.ADDRESS, this.PORT);
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          Response _response = TcpClientNotifiesMessageListenerOnSpec.this.callback.getResponse();
          boolean _notEquals = (!Objects.equal(_response, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    TcpClientListener _verify = Mockito.<TcpClientListener>verify(this.listener);
    _verify.messageReceived(this.responseMessage);
  }
  
  @Test
  @Named("disconnect")
  @Order(17)
  public void _disconnect() throws Exception {
    this.subject.close();
    TcpClientListener _verify = Mockito.<TcpClientListener>verify(this.listener);
    _verify.close();
  }
  
  @Test
  @Named("errors")
  @Order(18)
  public void _errors() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    this.server.shutdown();
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          String _error = TcpClientNotifiesMessageListenerOnSpec.this.callback.getError();
          boolean _notEquals = (!Objects.equal(_error, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    TcpClientListener _verify = Mockito.<TcpClientListener>verify(this.listener);
    Exception _isA = Matchers.<Exception>isA(Exception.class);
    _verify.receiveError(_isA);
  }
}
