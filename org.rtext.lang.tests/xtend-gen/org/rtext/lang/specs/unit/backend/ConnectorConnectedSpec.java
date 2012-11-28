package org.rtext.lang.specs.unit.backend;

import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Connected")
public class ConnectorConnectedSpec extends ConnectorSpec {
  @Test
  @Named("initially disconnected")
  @Order(15)
  public void _initiallyDisconnected() throws Exception {
    boolean _isConnected = this.subject.isConnected();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isConnected), Boolean.valueOf(false));
    Assert.assertTrue("\nExpected subject.connected => false but"
     + "\n     subject.connected is " + new StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("connected if process is running")
  @Order(16)
  public void _connectedIfProcessIsRunning() throws Exception {
    boolean _isRunning = this.processRunner.isRunning();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isRunning));
    _when.thenReturn(Boolean.valueOf(true));
    boolean _isConnected = this.subject.isConnected();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isConnected), Boolean.valueOf(true));
    Assert.assertTrue("\nExpected subject.connected => true but"
     + "\n     subject.connected is " + new StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("disconnected if process is not running")
  @Order(17)
  public void _disconnectedIfProcessIsNotRunning() throws Exception {
    boolean _isRunning = this.processRunner.isRunning();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(Boolean.valueOf(_isRunning));
    _when.thenReturn(Boolean.valueOf(false));
    boolean _isConnected = this.subject.isConnected();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isConnected), Boolean.valueOf(false));
    Assert.assertTrue("\nExpected subject.connected => false but"
     + "\n     subject.connected is " + new StringDescription().appendValue(Boolean.valueOf(_isConnected)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
  }
}
