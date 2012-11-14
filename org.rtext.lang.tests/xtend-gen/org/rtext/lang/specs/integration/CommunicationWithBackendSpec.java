package org.rtext.lang.specs.integration;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Response;
import org.rtext.lang.specs.util.BackendHelper;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Communication with Backend")
public class CommunicationWithBackendSpec {
  @Extension
  public BackendHelper _backendHelper = new Function0<BackendHelper>() {
    public BackendHelper apply() {
      BackendHelper _backendHelper = new BackendHelper();
      return _backendHelper;
    }
  }.apply();
  
  final String modelFile = "rtext/test/integration/model/test_metamodel.ect";
  
  @Test
  @Named("Executing commands synchronously")
  @Order(0)
  public void _executingCommandsSynchronously() throws Exception {
    this._backendHelper.startBackendFor(this.modelFile);
    this._backendHelper.executeSynchronousCommand();
    Response _response = this._backendHelper.getResponse();
    String _type = _response.getType();
    boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
    Assert.assertTrue("\nExpected response.type => \"response\" but"
     + "\n     response.type is " + new StringDescription().appendValue(_type).toString()
     + "\n     response is " + new StringDescription().appendValue(_response).toString()
     + "\n      is " + new StringDescription().appendValue(this._backendHelper).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("Executing commands asynchronously")
  @Order(1)
  public void _executingCommandsAsynchronously() throws Exception {
    this._backendHelper.startBackendFor(this.modelFile);
    this._backendHelper.executeAsynchronousCommand();
    Response _response = this._backendHelper.getResponse();
    String _type = _response.getType();
    boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
    Assert.assertTrue("\nExpected response.type => \"response\" but"
     + "\n     response.type is " + new StringDescription().appendValue(_type).toString()
     + "\n     response is " + new StringDescription().appendValue(_response).toString()
     + "\n      is " + new StringDescription().appendValue(this._backendHelper).toString() + "\n", _doubleArrow);
    
  }
}
