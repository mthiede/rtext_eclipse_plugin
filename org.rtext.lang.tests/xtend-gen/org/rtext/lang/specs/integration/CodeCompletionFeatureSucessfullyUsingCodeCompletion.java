package org.rtext.lang.specs.integration;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ProposalProvider;
import org.rtext.lang.specs.integration.CodeCompletionFeatureBackground;
import org.rtext.lang.specs.util.TestProposalAcceptor;

@RunWith(FeatureRunner.class)
@Named("Scenario: Sucessfully using code completion")
@SuppressWarnings("all")
public class CodeCompletionFeatureSucessfullyUsingCodeCompletion extends CodeCompletionFeatureBackground {
  @Test
  @Order(0)
  @Named("Given a model file \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void givenAModelFileRtextTestIntegrationModelTestMetamodelEct() {
    super.givenAModelFileRtextTestIntegrationModelTestMetamodelEct();
  }
  
  @Test
  @Order(1)
  @Named("And a running backend")
  public void andARunningBackend() {
    super.andARunningBackend();
  }
  
  @Test
  @Order(2)
  @Named("Given a proposal provider")
  public void givenAProposalProvider() {
    Connector _connector = this.b.getConnector();
    ProposalProvider _create = ProposalProvider.create(_connector);
    this.proposalProvider = _create;
  }
  
  @Test
  @Order(3)
  @Named("When I invoke the code completion after \\\"EPackage StatemachineMM {\\\\n\\\"")
  public void whenIInvokeTheCodeCompletionAfterEPackageStatemachineMMN() {
    StepArguments _stepArguments = new StepArguments("EPackage StatemachineMM {\\n");
    final StepArguments args = _stepArguments;
    IDocument _document = this.b.getDocument();
    String _first = JnarioIterableExtensions.<String>first(args);
    int _offsetAfter = this.b.offsetAfter(_first);
    TestProposalAcceptor _proposalAcceptor = this.b.getProposalAcceptor();
    this.proposalProvider.calculateProposals(_document, _offsetAfter, _proposalAcceptor);
  }
  
  @Test
  @Order(4)
  @Named("Then the proposals should be")
  public void thenTheProposalsShouldBe() {
    StepArguments _stepArguments = new StepArguments("EAnnotation\n\t    EClass\n\t    EClassifier\n\t    EDataType\n\t    EEnum\n\t    EGenericType\n\t    EPackage");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    String[] _split = _first.split("\r?\n");
    final Function1<String,String> _function = new Function1<String,String>() {
        public String apply(final String it) {
          String _trim = it.trim();
          return _trim;
        }
      };
    final List<String> expectedProposals = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(_split)), _function);
    ArrayList<String> _proposals = this.b.proposals();
    boolean _doubleArrow = Should.operator_doubleArrow(_proposals, expectedProposals);
    Assert.assertTrue("\nExpected b.proposals => expectedProposals but"
     + "\n     b.proposals is " + new StringDescription().appendValue(_proposals).toString()
     + "\n     b is " + new StringDescription().appendValue(this.b).toString()
     + "\n     expectedProposals is " + new StringDescription().appendValue(expectedProposals).toString() + "\n", _doubleArrow);
    
  }
  
  ProposalProvider proposalProvider;
}
