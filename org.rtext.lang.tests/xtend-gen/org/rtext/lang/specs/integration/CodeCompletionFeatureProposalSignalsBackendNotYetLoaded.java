package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.widgets.Display;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.proposals.ContentAssistProcessor;
import org.rtext.lang.specs.integration.CodeCompletionFeature;
import org.rtext.lang.specs.util.BackendHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Proposal signals backend not yet loaded")
@SuppressWarnings("all")
public class CodeCompletionFeatureProposalSignalsBackendNotYetLoaded extends CodeCompletionFeature {
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
  @Named("When I invoke the code completion after \\\"EPackage StatemachineMM {\\\\n\\\"")
  public void _whenIInvokeTheCodeCompletionAfterEPackageStatemachineMMN() {
    final StepArguments args = new StepArguments("EPackage StatemachineMM {\\n");
    final Connected _function = new Connected() {
      public Connector getConnector() {
        return CodeCompletionFeatureProposalSignalsBackendNotYetLoaded.this.b.connector;
      }
    };
    ContentAssistProcessor _create = ContentAssistProcessor.create(_function);
    this.proposalProvider = _create;
    Display _default = Display.getDefault();
    final Runnable _function_1 = new Runnable() {
      public void run() {
        String _first = JnarioIterableExtensions.<String>first(args);
        int _offsetAfter = CodeCompletionFeatureProposalSignalsBackendNotYetLoaded.this.b.offsetAfter(_first);
        ICompletionProposal[] _computeCompletionProposals = CodeCompletionFeatureProposalSignalsBackendNotYetLoaded.this.proposalProvider.computeCompletionProposals(CodeCompletionFeatureProposalSignalsBackendNotYetLoaded.this.b.document, _offsetAfter, 0);
        final Function1<ICompletionProposal, String> _function = new Function1<ICompletionProposal, String>() {
          public String apply(final ICompletionProposal it) {
            String _displayString = it.getDisplayString();
            return _displayString.trim();
          }
        };
        List<String> _map = ListExtensions.<ICompletionProposal, String>map(((List<ICompletionProposal>)Conversions.doWrapArray(_computeCompletionProposals)), _function);
        CodeCompletionFeatureProposalSignalsBackendNotYetLoaded.this.proposals = _map;
      }
    };
    _default.syncExec(_function_1);
  }
  
  @Test
  @Order(2)
  @Named("Then the proposals should be")
  public void _thenTheProposalsShouldBe() {
    final StepArguments args = new StepArguments("model not yet loaded\n");
    String _first = JnarioIterableExtensions.<String>first(args);
    String _trim = _first.trim();
    String[] _split = _trim.split("\r?\n");
    final Function1<String, String> _function = new Function1<String, String>() {
      public String apply(final String it) {
        return it.trim();
      }
    };
    final List<String> expectedProposals = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(_split)), _function);
    InputOutput.<List<String>>println(this.proposals);
    String _string = expectedProposals.toString();
    String _string_1 = this.proposals.toString();
    Assert.assertEquals(_string, _string_1);
  }
}
