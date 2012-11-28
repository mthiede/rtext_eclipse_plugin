package org.rtext.lang.specs.unit.backend;

import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

import com.google.common.base.Objects;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("connection state")
public class TcpClientConnectionStateSpec extends TcpClientSpec {
  @Test
  @Named("is initially disconnected")
  @Order(11)
  public void _isInitiallyDisconnected() throws Exception {
    boolean _isConnected = this.subject.isConnected();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isConnected), Boolean.valueOf(false));
    Assert.assertTrue("\nExpected subject.isConnected => false but"
     + "\n     subject.isConnected is " + new StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("is connected after successful connection to server")
  @Order(12)
  public void _isConnectedAfterSuccessfulConnectionToServer() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    boolean _isConnected = this.subject.isConnected();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isConnected), Boolean.valueOf(true));
    Assert.assertTrue("\nExpected subject.isConnected => true but"
     + "\n     subject.isConnected is " + new StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("is disconnected when server shuts down")
  @Order(13)
  public void _isDisconnectedWhenServerShutsDown() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    this.server.shutdown();
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
        public Boolean apply(final WaitConfig it) {
          String _error = TcpClientConnectionStateSpec.this.callback.getError();
          boolean _notEquals = (!Objects.equal(_error, null));
          return Boolean.valueOf(_notEquals);
        }
      };
    Wait.waitUntil(_function);
    boolean _isConnected = this.subject.isConnected();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isConnected), Boolean.valueOf(false));
    Assert.assertTrue("\nExpected subject.isConnected => false but"
     + "\n     subject.isConnected is " + new StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
}
