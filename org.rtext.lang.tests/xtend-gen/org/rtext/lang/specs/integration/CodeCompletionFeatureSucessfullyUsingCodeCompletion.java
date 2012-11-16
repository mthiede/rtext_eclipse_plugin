package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
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
import org.rtext.lang.specs.integration.CodeCompletionFeatureBackground;

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
  @Named("And a proposal provider")
  public void andAProposalProvider() {
    super.andAProposalProvider();
  }
  
  @Test
  @Order(3)
  @Named("When I invoke the code completion after \\\"EPackage StatemachineMM {\\\\n\\\"")
  public void whenIInvokeTheCodeCompletionAfterEPackageStatemachineMMN() {
    StepArguments _stepArguments = new StepArguments("EPackage StatemachineMM {\\n");
    final StepArguments args = _stepArguments;
    ContentAssistEvent __ = Should.<ContentAssistEvent>_();
    this.proposalProvider.assistSessionStarted(__);
    IDocument _document = this.b.getDocument();
    String _first = JnarioIterableExtensions.<String>first(args);
    int _offsetAfter = this.b.offsetAfter(_first);
    ICompletionProposal[] _computeCompletionProposals = this.proposalProvider.computeCompletionProposals(_document, _offsetAfter, 0);
    final Function1<ICompletionProposal,String> _function = new Function1<ICompletionProposal,String>() {
        public String apply(final ICompletionProposal it) {
          String _displayString = it.getDisplayString();
          return _displayString;
        }
      };
    List<String> _map = ListExtensions.<ICompletionProposal, String>map(((List<ICompletionProposal>)Conversions.doWrapArray(_computeCompletionProposals)), _function);
    this.proposals = _map;
    ContentAssistEvent ___1 = Should.<ContentAssistEvent>_();
    this.proposalProvider.assistSessionEnded(___1);
  }
  
  @Test
  @Order(4)
  @Named("Then the proposals should be")
  public void thenTheProposalsShouldBe() {
    StepArguments _stepArguments = new StepArguments("EAnnotation \nEClass\nEClassifier\nEDataType\nEEnum\nEGenericType\nEPackage\n\t");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    String _trim = _first.trim();
    String[] _split = _trim.split("\r?\n");
    final Function1<String,String> _function = new Function1<String,String>() {
        public String apply(final String it) {
          String _trim = it.trim();
          return _trim;
        }
      };
    final List<String> expectedProposals = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(_split)), _function);
    boolean _doubleArrow = Should.operator_doubleArrow(
      this.proposals, expectedProposals);
    Assert.assertTrue("\nExpected proposals => expectedProposals but"
     + "\n     proposals is " + new StringDescription().appendValue(this.proposals).toString()
     + "\n     expectedProposals is " + new StringDescription().appendValue(expectedProposals).toString() + "\n", _doubleArrow);
    
  }
}
