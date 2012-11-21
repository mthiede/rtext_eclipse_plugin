package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.Extension;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.editor.ContentAssistProcessor;
import org.rtext.lang.specs.integration.CodeCompletionFeature;
import org.rtext.lang.specs.util.BackendHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Proposal signals backend failure")
@SuppressWarnings("all")
public class CodeCompletionFeatureProposalSignalsBackendFailure extends CodeCompletionFeature {
  @Test
  @Order(0)
  @Named("Given a backend for \\\"rtext/test/integration/model/test.crashing_backend\\\"")
  public void givenABackendForRtextTestIntegrationModelTestCrashingBackend() {
    StepArguments _stepArguments = new StepArguments("rtext/test/integration/model/test.crashing_backend");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    String _absolutPath = this.b.absolutPath(_first);
    this.b.startBackendFor(_absolutPath);
  }
  
  @Test
  @Order(1)
  @Named("When I invoke the code completion after \\\"EPackage StatemachineMM {\\\\n\\\"")
  public void whenIInvokeTheCodeCompletionAfterEPackageStatemachineMMN() {
    StepArguments _stepArguments = new StepArguments("EPackage StatemachineMM {\\n");
    final StepArguments args = _stepArguments;
    final Function0<Connector> _function = new Function0<Connector>() {
        public Connector apply() {
          Connector _connector = CodeCompletionFeatureProposalSignalsBackendFailure.this.b.getConnector();
          return _connector;
        }
      };
    ContentAssistProcessor _create = ContentAssistProcessor.create(new Connected() {
        public Connector getConnector() {
          return _function.apply();
        }
    });
    this.proposalProvider = _create;
    ContentAssistEvent __ = Should.<ContentAssistEvent>_();
    this.proposalProvider.assistSessionStarted(__);
    IDocument _document = this.b.getDocument();
    String _first = JnarioIterableExtensions.<String>first(args);
    int _offsetAfter = this.b.offsetAfter(_first);
    ICompletionProposal[] _computeCompletionProposals = this.proposalProvider.computeCompletionProposals(_document, _offsetAfter, 0);
    final Function1<ICompletionProposal,String> _function_1 = new Function1<ICompletionProposal,String>() {
        public String apply(final ICompletionProposal it) {
          String _displayString = it.getDisplayString();
          String _trim = _displayString.trim();
          return _trim;
        }
      };
    List<String> _map = ListExtensions.<ICompletionProposal, String>map(((List<ICompletionProposal>)Conversions.doWrapArray(_computeCompletionProposals)), _function_1);
    this.proposals = _map;
    ContentAssistEvent ___1 = Should.<ContentAssistEvent>_();
    this.proposalProvider.assistSessionEnded(___1);
  }
  
  @Test
  @Order(2)
  @Named("Then the proposals should be")
  public void thenTheProposalsShouldBe() {
    StepArguments _stepArguments = new StepArguments("Cannot load backend\n\t");
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
    boolean _doubleArrow = Should.operator_doubleArrow(this.proposals, expectedProposals);
    Assert.assertTrue("\nExpected proposals => expectedProposals but"
     + "\n     proposals => expectedProposals is " + new StringDescription().appendValue(Boolean.valueOf(_doubleArrow)).toString() + "\n", _doubleArrow);
    
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
