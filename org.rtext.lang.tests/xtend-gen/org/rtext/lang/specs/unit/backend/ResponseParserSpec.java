package org.rtext.lang.specs.unit.backend;

import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.runner.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Progress;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.backend2.ResponseParser;
import org.rtext.lang.specs.util.TestCallBack;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("ResponseParser")
public class ResponseParserSpec {
  @Subject
  public ResponseParser subject;
  
  final TestCallBack callback = new Function0<TestCallBack>() {
    public TestCallBack apply() {
      TestCallBack _testCallBack = new TestCallBack();
      return _testCallBack;
    }
  }.apply();
  
  @Test
  @Named("parses generic response")
  @Order(0)
  public void _parsesGenericResponse() throws Exception {
    this.parse("{\"type\":\"response\", \"invocation_id\":111, \"problems\":[], \"total_problems\":0}");
    Response _response = this.callback.getResponse();
    final Procedure1<Response> _function = new Procedure1<Response>() {
        public void apply(final Response it) {
          String _type = it.getType();
          boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
          Assert.assertTrue("\nExpected type => \"response\" but"
           + "\n     type is " + new StringDescription().appendValue(_type).toString() + "\n", _doubleArrow);
          
          int _invocationId = it.getInvocationId();
          boolean _doubleArrow_1 = Should.operator_doubleArrow(Integer.valueOf(_invocationId), Integer.valueOf(111));
          Assert.assertTrue("\nExpected invocationId => 111 but"
           + "\n     invocationId is " + new StringDescription().appendValue(Integer.valueOf(_invocationId)).toString() + "\n", _doubleArrow_1);
          
        }
      };
    ObjectExtensions.<Response>operator_doubleArrow(_response, _function);
  }
  
  @Test
  @Named("parses progress")
  @Order(1)
  public void _parsesProgress() throws Exception {
    this.parse("{\"type\":\"progress\",\"invocation_id\":111,\"percentage\":100}");
    List<Progress> _progress = this.callback.getProgress();
    Progress _first = JnarioIterableExtensions.<Progress>first(_progress);
    final Procedure1<Progress> _function = new Procedure1<Progress>() {
        public void apply(final Progress it) {
          boolean _doubleArrow = Should.operator_doubleArrow(it, Progress.class);
          Assert.assertTrue("\nExpected it => typeof(Progress) but"
           + "\n     it is " + new StringDescription().appendValue(it).toString() + "\n", _doubleArrow);
          
          String _type = it.getType();
          boolean _doubleArrow_1 = Should.operator_doubleArrow(_type, "progress");
          Assert.assertTrue("\nExpected type => \"progress\" but"
           + "\n     type is " + new StringDescription().appendValue(_type).toString() + "\n", _doubleArrow_1);
          
        }
      };
    ObjectExtensions.<Progress>operator_doubleArrow(_first, _function);
  }
  
  public boolean parse(final String input) {
    boolean _parse = this.subject.parse(input, this.callback);
    return _parse;
  }
}
