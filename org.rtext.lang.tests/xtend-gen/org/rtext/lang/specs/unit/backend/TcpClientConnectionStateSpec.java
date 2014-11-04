package org.rtext.lang.specs.unit.backend;

import com.google.common.base.Objects;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.TcpClientSpec;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

@Named("connection state")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class TcpClientConnectionStateSpec extends TcpClientSpec {
  @Test
  @Named("is initially disconnected")
  @Order(1)
  public void _isInitiallyDisconnected() throws Exception {
    boolean _isConnected = this.subject.isConnected();
    Assert.assertTrue("\nExpected subject.isConnected => false but"
     + "\n     subject.isConnected is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isConnected), false));
    
  }
  
  @Test
  @Named("is connected after successful connection to server")
  @Order(2)
  public void _isConnectedAfterSuccessfulConnectionToServer() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    boolean _isConnected = this.subject.isConnected();
    Assert.assertTrue("\nExpected subject.isConnected => true but"
     + "\n     subject.isConnected is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isConnected), true));
    
  }
  
  @Test
  @Named("is disconnected when server shuts down")
  @Order(3)
  public void _isDisconnectedWhenServerShutsDown() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    this.server.shutdown();
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig, Boolean> _function = new Function1<WaitConfig, Boolean>() {
      public Boolean apply(final WaitConfig it) {
        String _error = TcpClientConnectionStateSpec.this.callback.getError();
        return Boolean.valueOf((!Objects.equal(_error, null)));
      }
    };
    Wait.waitUntil(_function);
    boolean _isConnected = this.subject.isConnected();
    Assert.assertTrue("\nExpected subject.isConnected => false but"
     + "\n     subject.isConnected is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isConnected), false));
    
  }
}
