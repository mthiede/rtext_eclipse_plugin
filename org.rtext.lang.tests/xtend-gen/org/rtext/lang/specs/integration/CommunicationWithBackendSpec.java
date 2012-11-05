package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.Response;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Communication with Backend")
public class CommunicationWithBackendSpec {
  final Connector connector = new Function0<Connector>() {
    public Connector apply() {
      Connector _connector = new Connector();
      return _connector;
    }
  }.apply();
  
  @Test
  @Named("Loads specified file")
  @Order(0)
  public void _loadsSpecifiedFile() throws Exception {
    this.connect("/model/test_metamodel.ect");
    final Response response = this.loadModel();
    String _type = response.getType();
    boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
    Assert.assertTrue("\nExpected response.type \t\t\t=> \"response\" but"
     + "\n     response.type is " + new StringDescription().appendValue(_type).toString()
     + "\n     response is " + new StringDescription().appendValue(response).toString() + "\n", _doubleArrow);
    
    List<String> _problems = response.getProblems();
    int _size = _problems.size();
    boolean _doubleArrow_1 = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected response.problems.size \t=> 0 but"
     + "\n     response.problems.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     response.problems is " + new StringDescription().appendValue(_problems).toString()
     + "\n     response is " + new StringDescription().appendValue(response).toString() + "\n", _doubleArrow_1);
    
  }
  
  public void connect(final String filePath) {
    this.connector.connect(filePath);
  }
  
  public Response loadModel() {
    Command _command = new Command("load_model");
    Response _execute = this.connector.execute(_command);
    return _execute;
  }
}
