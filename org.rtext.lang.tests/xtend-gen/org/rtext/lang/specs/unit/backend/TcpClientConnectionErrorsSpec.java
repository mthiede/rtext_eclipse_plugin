package org.rtext.lang.specs.unit.backend;

import com.google.common.base.Objects;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.jnario.lib.Assert;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.BackendException;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.TcpClientSpec;
import org.rtext.lang.specs.util.Commands;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

@Named("connection errors")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class TcpClientConnectionErrorsSpec extends TcpClientSpec {
  @Test
  @Named("throws exception when server does not exist")
  @Order(1)
  public void _throwsExceptionWhenServerDoesNotExist() throws Exception {
    boolean expectedException = false;
    String message = "";
    try{
      this.subject.connect("6.6.6.6", 6666);
      message = "Expected " + BackendException.class.getName() + " for \n     subject.connect(\"6.6.6.6\", 6666)\n with:"
       + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString();
    }catch(BackendException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
  }
  
  @Test
  @Named("throws exception when server is not running")
  @Order(2)
  public void _throwsExceptionWhenServerIsNotRunning() throws Exception {
    this.server.shutdown();
    boolean expectedException = false;
    String message = "";
    try{
      this.subject.connect(this.ADDRESS, this.PORT);
      message = "Expected " + BackendException.class.getName() + " for \n     subject.connect(ADDRESS, PORT)\n with:"
       + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
       + "\n     ADDRESS is " + new org.hamcrest.StringDescription().appendValue(this.ADDRESS).toString()
       + "\n     PORT is " + new org.hamcrest.StringDescription().appendValue(this.PORT).toString();
    }catch(BackendException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
  }
  
  @Test
  @Named("callback receives error if connection is closed")
  @Order(3)
  public void _callbackReceivesErrorIfConnectionIsClosed() throws Exception {
    this.subject.connect(this.ADDRESS, this.PORT);
    this.server.shutdown();
    this.subject.<Response>sendRequest(Commands.ANY_COMMAND, this.callback);
    final Function1<WaitConfig, Boolean> _function = new Function1<WaitConfig, Boolean>() {
      public Boolean apply(final WaitConfig it) {
        String _error = TcpClientConnectionErrorsSpec.this.callback.getError();
        return Boolean.valueOf((!Objects.equal(_error, null)));
      }
    };
    Wait.waitUntil(_function);
  }
}
