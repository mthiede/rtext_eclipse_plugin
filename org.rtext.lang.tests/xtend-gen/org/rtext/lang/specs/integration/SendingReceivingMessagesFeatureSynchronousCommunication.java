package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.hamcrest.StringDescription;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.Extension;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.commands.Response;
import org.rtext.lang.proposals.ContentAssistProcessor;
import org.rtext.lang.specs.integration.SendingReceivingMessagesFeature;
import org.rtext.lang.specs.util.BackendHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Synchronous communication")
@SuppressWarnings("all")
public class SendingReceivingMessagesFeatureSynchronousCommunication extends SendingReceivingMessagesFeature {
  @Extension
  public BackendHelper b = new Function0<BackendHelper>() {
    public BackendHelper apply() {
      BackendHelper _backendHelper = new BackendHelper();
      return _backendHelper;
    }
  }.apply();
  
  String modelFile;
  
  List<String> proposals;
  
  ContentAssistProcessor proposalProvider;
  
  @Test
  @Order(0)
  @Named("Given a backend for \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void givenABackendForRtextTestIntegrationModelTestMetamodelEct() {
    StepArguments _stepArguments = new StepArguments("rtext/test/integration/model/test_metamodel.ect");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    String _absolutPath = this.b.absolutPath(_first);
    this.b.startBackendFor(_absolutPath);
  }
  
  @Test
  @Order(1)
  @Named("When I send an synchronous message")
  public void whenISendAnSynchronousMessage() {
    this.b.executeSynchronousCommand();
  }
  
  @Test
  @Order(2)
  @Named("Then I get a response")
  public void thenIGetAResponse() {
    Response _response = this.b.getResponse();
    String _type = _response.getType();
    boolean _doubleArrow = Should.operator_doubleArrow(_type, "response");
    Assert.assertTrue("\nExpected response.type => \"response\" but"
     + "\n     response.type is " + new StringDescription().appendValue(_type).toString()
     + "\n     response is " + new StringDescription().appendValue(_response).toString() + "\n", _doubleArrow);
    
  }
}
