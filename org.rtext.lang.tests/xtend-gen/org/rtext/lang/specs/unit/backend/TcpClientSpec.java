package org.rtext.lang.specs.unit.backend;

import java.util.concurrent.CountDownLatch;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.hamcrest.StringDescription;
import org.jnario.runner.Contains;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.rtext.lang.backend.TcpClient;
import org.rtext.lang.backend.TcpClientListener;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.specs.util.TcpTestServer;
import org.rtext.lang.specs.util.TestCallBack;

@Contains({ TcpClientSendingRequestsSpec.class, TcpClientConnectionErrorsSpec.class, TcpClientConnectionStateSpec.class, TcpClientNotifiesMessageListenerOnSpec.class })
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("TcpClient")
@CreateWith(value = MockInjector.class)
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
  
  final int ANOTHER_PORT = new Function0<Integer>() {
    public Integer apply() {
      int _plus = (TcpClientSpec.this.PORT + 1);
      return _plus;
    }
  }.apply();
  
  final String ADDRESS = "127.0.0.1";
  
  final CountDownLatch startSignal = new Function0<CountDownLatch>() {
    public CountDownLatch apply() {
      CountDownLatch _countDownLatch = new CountDownLatch(2);
      return _countDownLatch;
    }
  }.apply();
  
  final TcpTestServer server = new Function0<TcpTestServer>() {
    public TcpTestServer apply() {
      TcpTestServer _tcpTestServer = new TcpTestServer(TcpClientSpec.this.ADDRESS, TcpClientSpec.this.PORT);
      return _tcpTestServer;
    }
  }.apply();
  
  final TcpTestServer anotherSever = new Function0<TcpTestServer>() {
    public TcpTestServer apply() {
      TcpTestServer _tcpTestServer = new TcpTestServer(TcpClientSpec.this.ADDRESS, TcpClientSpec.this.ANOTHER_PORT);
      return _tcpTestServer;
    }
  }.apply();
  
  final TestCallBack<Response> callback = new Function0<TestCallBack<Response>>() {
    public TestCallBack<Response> apply() {
      TestCallBack<Response> _testCallBack = new TestCallBack<Response>();
      return _testCallBack;
    }
  }.apply();
  
  @Mock
  TcpClientListener listener;
  
  @Before
  public void before() throws Exception {
    this.server.start(this.startSignal);
    this.anotherSever.start(this.startSignal);
    InputOutput.<String>println("wait for server");
    this.startSignal.await();
    TcpClient _create = TcpClient.create(this.listener);
    this.subject = _create;
  }
  
  @Test
  @Named("subject.connect[ADDRESS, INVALID_PORT] throws IllegalArgumentException")
  @Order(1)
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
    this.anotherSever.shutdown();
  }
  
  public void is(final Response actual, final CharSequence expected) {
    String _string = expected.toString();
    Assert.assertEquals(_string, actual);
  }
}
