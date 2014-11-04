package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.proposals.ContentAssistProcessor;
import org.rtext.lang.specs.integration.SendingReceivingMessagesFeature;
import org.rtext.lang.specs.util.BackendHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Synchronous communication")
@SuppressWarnings("all")
public class SendingReceivingMessagesFeatureSynchronousCommunication extends SendingReceivingMessagesFeature {
  @Extension
  @org.jnario.runner.Extension
  public BackendHelper b = new BackendHelper();
  
  String modelFile;
  
  List<String> proposals;
  
  ContentAssistProcessor proposalProvider;
  
  @Test
  @Order(0)
  @Named("Given a backend for \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void _givenABackendForRtextTestIntegrationModelTestMetamodelEct() {
    final StepArguments args = new StepArguments("rtext/test/integration/model/test_metamodel.ect");
    String _first = JnarioIterableExtensions.<String>first(args);
    String _absolutPath = this.b.absolutPath(_first);
    this.b.startBackendFor(_absolutPath);
  }
  
  @Test
  @Order(1)
  @Named("When I send an synchronous message")
  public void _whenISendAnSynchronousMessage() {
    this.b.executeSynchronousCommand();
  }
  
  @Test
  @Order(2)
  @Named("Then I get a response")
  public void _thenIGetAResponse() {
    String _type = this.b.response.getType();
    Assert.assertTrue("\nExpected response.type => \"response\" but"
     + "\n     response.type is " + new org.hamcrest.StringDescription().appendValue(_type).toString()
     + "\n     response is " + new org.hamcrest.StringDescription().appendValue(this.b.response).toString() + "\n", Should.<String>operator_doubleArrow(_type, "response"));
    
  }
}
