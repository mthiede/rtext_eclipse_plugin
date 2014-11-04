package org.rtext.lang.specs.unit.backend;

import org.jnario.lib.Assert;
import org.jnario.lib.Should;
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
import org.rtext.lang.backend.Connection;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@Named("Busy")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ConnectorBusySpec extends ConnectorSpec {
  @Test
  @Named("if waiting for response")
  @Order(1)
  public void _ifWaitingForResponse() throws Exception {
    boolean _isBusy = this.subject.isBusy();
    boolean _doubleArrow = Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isBusy), false);
    Assert.assertTrue("\nExpected subject.busy => false but"
     + "\n     subject.busy is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isBusy)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
    final Answer<Callback<Response>> _function = new Answer<Callback<Response>>() {
      public Callback<Response> answer(final InvocationOnMock it) throws Throwable {
        Object[] _arguments = it.getArguments();
        Object _get = _arguments[1];
        return ConnectorBusySpec.this.callback = ((Callback) _get);
      }
    };
    Stubber _doAnswer = Mockito.doAnswer(_function);
    Connection _when = _doAnswer.<Connection>when(this.connection);
    Command _any = Matchers.<Command>any();
    Callback _any_1 = Matchers.<Callback>any();
    _when.<Response>sendRequest(_any, _any_1);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    boolean _isBusy_1 = this.subject.isBusy();
    boolean _doubleArrow_1 = Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isBusy_1), true);
    Assert.assertTrue("\nExpected subject.busy => true but"
     + "\n     subject.busy is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isBusy_1)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_1);
    
    Response _response = new Response(0, "");
    this.callback.handleResponse(_response);
    boolean _isBusy_2 = this.subject.isBusy();
    Assert.assertTrue("\nExpected subject.busy => false but"
     + "\n     subject.busy is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isBusy_2)).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_isBusy_2), false));
    
  }
}
