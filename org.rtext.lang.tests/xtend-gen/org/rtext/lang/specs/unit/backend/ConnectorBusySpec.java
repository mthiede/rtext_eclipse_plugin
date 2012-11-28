package org.rtext.lang.specs.unit.backend;

import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
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

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Busy")
public class ConnectorBusySpec extends ConnectorSpec {
  @Test
  @Named("if waiting for response")
  @Order(14)
  public void _ifWaitingForResponse() throws Exception {
    boolean _isBusy = this.subject.isBusy();
    boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_isBusy), Boolean.valueOf(false));
    Assert.assertTrue("\nExpected subject.busy => false but"
     + "\n     subject.busy is " + new StringDescription().appendValue(Boolean.valueOf(_isBusy)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow);
    
    final Function1<InvocationOnMock,Callback<Response>> _function = new Function1<InvocationOnMock,Callback<Response>>() {
        public Callback<Response> apply(final InvocationOnMock it) {
          Object[] _arguments = it.getArguments();
          Object _get = ((List<Object>)Conversions.doWrapArray(_arguments)).get(1);
          Callback<Response> _callback = ConnectorBusySpec.this.callback = ((Callback) _get);
          return _callback;
        }
      };
    Stubber _doAnswer = Mockito.doAnswer(new Answer<Callback<Response>>() {
        public Callback<Response> answer(InvocationOnMock invocation) {
          return _function.apply(invocation);
        }
    });
    Connection _when = _doAnswer.<Connection>when(this.connection);
    Command _any = Matchers.<Command>any();
    Callback _any_1 = Matchers.<Callback>any();
    _when.sendRequest(_any, _any_1);
    this.subject.<Response>execute(this.anyCommand, this.callback);
    boolean _isBusy_1 = this.subject.isBusy();
    boolean _doubleArrow_1 = Should.operator_doubleArrow(Boolean.valueOf(_isBusy_1), Boolean.valueOf(true));
    Assert.assertTrue("\nExpected subject.busy => true but"
     + "\n     subject.busy is " + new StringDescription().appendValue(Boolean.valueOf(_isBusy_1)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_1);
    
    Response _response = new Response(0, "");
    this.callback.handleResponse(_response);
    boolean _isBusy_2 = this.subject.isBusy();
    boolean _doubleArrow_2 = Should.operator_doubleArrow(Boolean.valueOf(_isBusy_2), Boolean.valueOf(false));
    Assert.assertTrue("\nExpected subject.busy => false but"
     + "\n     subject.busy is " + new StringDescription().appendValue(Boolean.valueOf(_isBusy_2)).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString() + "\n", _doubleArrow_2);
    
  }
}
