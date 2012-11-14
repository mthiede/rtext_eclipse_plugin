package org.rtext.lang.specs.unit.backend;

import java.util.concurrent.CountDownLatch;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.hamcrest.StringDescription;
import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.backend2.TcpClient;
import org.rtext.lang.specs.unit.backend.TcpClientConnectionErrorsSpec;
import org.rtext.lang.specs.unit.backend.TcpClientConnectionStateSpec;
import org.rtext.lang.specs.unit.backend.TcpClientSendingRequestsSpec;
import org.rtext.lang.specs.util.TcpTestServer;
import org.rtext.lang.specs.util.TestCallBack;

@Contains({ TcpClientSendingRequestsSpec.class, TcpClientConnectionErrorsSpec.class, TcpClientConnectionStateSpec.class })
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("TcpClient")
public class TcpClientSpec {
  public TcpClient subject;
  
  int INVALID_PORT = new Function0<Integer>() {
    public Integer apply() {
      int _minus = (-1);
      return _minus;
    }
  }.apply();
  
  final String progressMessage = "{\"type\":\"progress\",\"invocation_id\":111,\"percentage\":100}";
  
  final String responseMessage = "{\"type\":\"response\", \"invocation_id\":111, \"problems\":[], \"total_problems\":0}";
  
  final int PORT = 12345;
  
  final String ADDRESS = "127.0.0.1";
  
  final CountDownLatch startSignal = new Function0<CountDownLatch>() {
    public CountDownLatch apply() {
      CountDownLatch _countDownLatch = new CountDownLatch(1);
      return _countDownLatch;
    }
  }.apply();
  
  final TcpTestServer server = new Function0<TcpTestServer>() {
    public TcpTestServer apply() {
      TcpTestServer _tcpTestServer = new TcpTestServer(TcpClientSpec.this.ADDRESS, TcpClientSpec.this.PORT);
      return _tcpTestServer;
    }
  }.apply();
  
  final TestCallBack<Response> callback = new Function0<TestCallBack<Response>>() {
    public TestCallBack<Response> apply() {
      TestCallBack<Response> _testCallBack = new TestCallBack<Response>();
      return _testCallBack;
    }
  }.apply();
  
  @Before
  public void before() throws Exception {
    this.server.start(this.startSignal);
    InputOutput.<String>println("wait for server");
    this.startSignal.await();
    TcpClient _create = TcpClient.create();
    this.subject = _create;
  }
  
  @Test
  @Named("subject.connect[ADDRESS, INVALID_PORT] throws IllegalArgumentException")
  @Order(0)
  public void _subjectConnectADDRESSINVALIDPORTThrowsIllegalArgumentException() throws Exception {
    try{
      this.subject.connect(this.ADDRESS, this.INVALID_PORT);
      Assert.fail("Expected " + IllegalArgumentException.class.getName() + " in \n     subject.connect(ADDRESS, INVALID_PORT)\n with:"
       + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
       + "\n     ADDRESS is " + new StringDescription().appendValue(this.ADDRESS).toString()
       + "\n     INVALID_PORT is " + new StringDescription().appendValue(this.INVALID_PORT).toString());
    }catch(IllegalArgumentException e){
    }
  }
  
  @After
  public void after() throws Exception {
    this.server.shutdown();
  }
  
  public void is(final Response actual, final CharSequence expected) {
    String _string = expected.toString();
    Assert.assertEquals(_string, actual);
  }
}
