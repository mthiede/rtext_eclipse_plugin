package org.rtext.lang.specs.integration;

import com.google.common.base.Objects;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.CachingConnectorProvider;
import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ConnectorProvider;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.specs.util.TestCallBack;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Communication with Backend")
public class CommunicationWithBackendSpec {
  @Extension
  public TestFileLocator _testFileLocator = new Function0<TestFileLocator>() {
    public TestFileLocator apply() {
      TestFileLocator _testFileLocator = new TestFileLocator("backends/head");
      return _testFileLocator;
    }
  }.apply();
  
  final String modelFile = new Function0<String>() {
    public String apply() {
      String _absolutPath = CommunicationWithBackendSpec.this._testFileLocator.absolutPath("rtext/test/integration/model/test_metamodel.ect");
      return _absolutPath;
    }
  }.apply();
  
  final ConnectorProvider connectorProvider = new Function0<ConnectorProvider>() {
    public ConnectorProvider apply() {
      ConnectorProvider _create = CachingConnectorProvider.create();
      return _create;
    }
  }.apply();
  
  final TestCallBack callback = new Function0<TestCallBack>() {
    public TestCallBack apply() {
      TestCallBack _testCallBack = new TestCallBack();
      return _testCallBack;
    }
  }.apply();
  
  Connector connector;
  
  Response response;
  
  @Test
  @Named("Executing commands synchronously")
  @Order(0)
  public void _executingCommandsSynchronously() throws Exception {
    this.startBackendFor(this.modelFile);
    this.executeSynchronousCommand();
    String _type = this.response.getType();
    boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
    Assert.assertTrue("\nExpected response.type => \"response\" but"
     + "\n     response.type is " + new StringDescription().appendValue(_type).toString()
     + "\n     response is " + new StringDescription().appendValue(this.response).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("Executing commands asynchronously")
  @Order(1)
  public void _executingCommandsAsynchronously() throws Exception {
    this.startBackendFor(this.modelFile);
    this.executeAsynchronousCommand();
    String _type = this.response.getType();
    boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
    Assert.assertTrue("\nExpected response.type => \"response\" but"
     + "\n     response.type is " + new StringDescription().appendValue(_type).toString()
     + "\n     response is " + new StringDescription().appendValue(this.response).toString() + "\n", _doubleArrow);
    
  }
  
  @After
  public void after() throws Exception {
    this.connectorProvider.dispose();
  }
  
  public Connector startBackendFor(final String filePath) {
    Connector _get = this.connectorProvider.get(filePath);
    Connector _connector = this.connector = _get;
    return _connector;
  }
  
  public Response executeSynchronousCommand() {
    try {
      Command _command = new Command(1, "request", "load_model");
      Response _execute = this.connector.execute(_command);
      Response _response = this.response = _execute;
      return _response;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Response executeAsynchronousCommand() {
    Response _xblockexpression = null;
    {
      final Function1<WaitConfig,Boolean> _function = new Function1<WaitConfig,Boolean>() {
          public Boolean apply(final WaitConfig it) {
            try {
              boolean _xblockexpression = false;
              {
                Command _command = new Command(2, "request", "load_model");
                CommunicationWithBackendSpec.this.connector.execute(_command, CommunicationWithBackendSpec.this.callback);
                Response _response = CommunicationWithBackendSpec.this.callback.getResponse();
                boolean _notEquals = (!Objects.equal(_response, null));
                _xblockexpression = (_notEquals);
              }
              return Boolean.valueOf(_xblockexpression);
            } catch (Exception _e) {
              throw Exceptions.sneakyThrow(_e);
            }
          }
        };
      Wait.waitUntil(_function);
      Response _response = this.callback.getResponse();
      Response _response_1 = this.response = _response;
      _xblockexpression = (_response_1);
    }
    return _xblockexpression;
  }
}
