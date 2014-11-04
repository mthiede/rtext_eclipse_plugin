package org.rtext.lang.specs.unit.backend;

import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@Named("Connected")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ConnectorConnectedSpec extends ConnectorSpec {
  @Test
  @Named("initially disconnected")
  @Order(1)
  public void _initiallyDisconnected() throws Exception {
    boolean _isConnected = this.subject.isConnected();
    Assert.assertTrue("\nExpected subject.connected => false but"
     + "\n     subject.connected is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isConnected), false));
    
  }
  
  @Test
  @Named("connected if process is running")
  @Order(2)
  public void _connectedIfProcessIsRunning() throws Exception {
    boolean _isRunning = this.processRunner.isRunning();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isRunning));
    _when.thenReturn(Boolean.valueOf(true));
    boolean _isConnected = this.subject.isConnected();
    Assert.assertTrue("\nExpected subject.connected => true but"
     + "\n     subject.connected is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isConnected), true));
    
  }
  
  @Test
  @Named("disconnected if process is not running")
  @Order(3)
  public void _disconnectedIfProcessIsNotRunning() throws Exception {
    boolean _isRunning = this.processRunner.isRunning();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isRunning));
    _when.thenReturn(Boolean.valueOf(false));
    boolean _isConnected = this.subject.isConnected();
    Assert.assertTrue("\nExpected subject.connected => false but"
     + "\n     subject.connected is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isConnected), false));
    
  }
}
