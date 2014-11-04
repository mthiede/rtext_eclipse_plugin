package org.rtext.lang.specs.unit.backend;

import java.util.concurrent.CountDownLatch;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.jnario.lib.Assert;
import org.jnario.runner.Contains;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.rtext.lang.backend.TcpClient;
import org.rtext.lang.backend.TcpClientListener;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.TcpClientConnectionErrorsSpec;
import org.rtext.lang.specs.unit.backend.TcpClientConnectionStateSpec;
import org.rtext.lang.specs.unit.backend.TcpClientNotifiesMessageListenerOnSpec;
import org.rtext.lang.specs.unit.backend.TcpClientSendingRequestsSpec;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.specs.util.TcpTestServer;
import org.rtext.lang.specs.util.TestCallBack;

@Contains({ TcpClientSendingRequestsSpec.class, TcpClientConnectionErrorsSpec.class, TcpClientConnectionStateSpec.class, TcpClientNotifiesMessageListenerOnSpec.class })
@CreateWith(MockInjector.class)
@Named("TcpClient")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class TcpClientSpec {
  public TcpClient subject;
  
  int INVALID_PORT = (-1);
  
  final String progressMessage = "{\"type\":\"progress\",\"invocation_id\":111,\"percentage\":100}";
  
  final String responseMessage = "{\"type\":\"response\", \"invocation_id\":111, \"problems\":[], \"total_problems\":0}";
  
  final int PORT = 12345;
  
  final int ANOTHER_PORT = (this.PORT + 1);
  
  final String ADDRESS = "127.0.0.1";
  
  final CountDownLatch startSignal = new CountDownLatch(2);
  
  final TcpTestServer server = new TcpTestServer(this.ADDRESS, this.PORT);
  
  final TcpTestServer anotherSever = new TcpTestServer(this.ADDRESS, this.ANOTHER_PORT);
  
  final TestCallBack<Response> callback = new TestCallBack<Response>();
  
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
    boolean expectedException = false;
    String message = "";
    try{
      this.subject.connect(this.ADDRESS, this.INVALID_PORT);
      message = "Expected " + IllegalArgumentException.class.getName() + " for \n     subject.connect(ADDRESS, INVALID_PORT)\n with:"
       + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
       + "\n     ADDRESS is " + new org.hamcrest.StringDescription().appendValue(this.ADDRESS).toString()
       + "\n     INVALID_PORT is " + new org.hamcrest.StringDescription().appendValue(this.INVALID_PORT).toString();
    }catch(IllegalArgumentException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
  }
  
  @After
  public void after() throws Exception {
    this.server.shutdown();
    this.anotherSever.shutdown();
  }
  
  public void is(final Response actual, final CharSequence expected) {
    String _string = expected.toString();
    org.junit.Assert.assertEquals(_string, actual);
  }
}
