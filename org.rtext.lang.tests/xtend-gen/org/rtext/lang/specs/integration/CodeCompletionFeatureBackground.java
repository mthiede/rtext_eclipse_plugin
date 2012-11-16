package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.Extension;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.editor.ContentAssistProcessor;
import org.rtext.lang.specs.util.BackendHelper;

@RunWith(FeatureRunner.class)
@Named("Background:")
@SuppressWarnings("all")
public class CodeCompletionFeatureBackground {
  @Test
  @Order(0)
  @Named("Given a model file \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void givenAModelFileRtextTestIntegrationModelTestMetamodelEct() {
    StepArguments _stepArguments = new StepArguments("rtext/test/integration/model/test_metamodel.ect");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    this.modelFile = _first;
  }
  
  @Test
  @Order(1)
  @Named("And a running backend")
  public void andARunningBackend() {
    this.b.startBackendFor(this.modelFile);
  }
  
  @Test
  @Order(2)
  @Named("And a proposal provider")
  public void andAProposalProvider() {
    final Function0<Connector> _function = new Function0<Connector>() {
        public Connector apply() {
          Connector _connector = CodeCompletionFeatureBackground.this.b.getConnector();
          return _connector;
        }
      };
    ContentAssistProcessor _create = ContentAssistProcessor.create(new Connected() {
        public Connector getConnector() {
          return _function.apply();
        }
    });
    this.proposalProvider = _create;
  }
  
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
}
