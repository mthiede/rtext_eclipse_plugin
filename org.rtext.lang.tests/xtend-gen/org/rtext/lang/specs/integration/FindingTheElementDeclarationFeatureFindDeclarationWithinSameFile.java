package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
import org.rtext.lang.backend.Connector;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.editor.HyperlinkDetector;
import org.rtext.lang.proposals.ContentAssistProcessor;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeature;
import org.rtext.lang.specs.util.BackendHelper;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.WorkspaceHelper;
import org.rtext.lang.util.Workbenches;

@RunWith(FeatureRunner.class)
@Named("Scenario: Find declaration within same file")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureFindDeclarationWithinSameFile extends FindingTheElementDeclarationFeature {
  HyperlinkDetector hyperLinkDetector;
  
  List<IHyperlink> hyperlinks;
  
  @Extension
  public WorkspaceHelper _workspaceHelper = new Function0<WorkspaceHelper>() {
    public WorkspaceHelper apply() {
      WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
      return _workspaceHelper;
    }
  }.apply();
  
  @Extension
  public TestFileLocator _testFileLocator = new Function0<TestFileLocator>() {
    public TestFileLocator apply() {
      TestFileLocator _default = TestFileLocator.getDefault();
      return _default;
    }
  }.apply();
  
  @Extension
  public BackendHelper _backendHelper = new Function0<BackendHelper>() {
    public BackendHelper apply() {
      BackendHelper _backendHelper = new BackendHelper();
      return _backendHelper;
    }
  }.apply();
  
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
  @Named("Given a project \\\"test\\\" linked to \\\"rtext/test/integration/model/\\\"")
  public void givenAProjectTestLinkedToRtextTestIntegrationModel() {
    StepArguments _stepArguments = new StepArguments("test", "rtext/test/integration/model/");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    String _second = JnarioIterableExtensions.<String>second(args);
    String _absolutPath = this._backendHelper.absolutPath(_second);
    this._workspaceHelper.createProject(_first, _absolutPath);
  }
  
  @Test
  @Order(1)
  @Named("And a backend for \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void andABackendForRtextTestIntegrationModelTestMetamodelEct() {
    StepArguments _stepArguments = new StepArguments("rtext/test/integration/model/test_metamodel.ect");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    String _absolutPath = this.b.absolutPath(_first);
    this.b.startBackendFor(_absolutPath);
  }
  
  @Test
  @Order(2)
  @Named("And a hyperlink detector")
  public void andAHyperlinkDetector() {
    final Function0<Connector> _function = new Function0<Connector>() {
        public Connector apply() {
          return FindingTheElementDeclarationFeatureFindDeclarationWithinSameFile.this.b.getConnector();
        }
      };
    HyperlinkDetector _hyperlinkDetector = new HyperlinkDetector(new Connected() {
        public Connector getConnector() {
          return _function.apply();
        }
    });
    this.hyperLinkDetector = _hyperlinkDetector;
  }
  
  @Test
  @Order(3)
  @Named("When I open the definition of \\\"/StatemachineMM/State\\\"")
  public void whenIOpenTheDefinitionOfStatemachineMMState() {
    StepArguments _stepArguments = new StepArguments("/StatemachineMM/State");
    final StepArguments args = _stepArguments;
    IDocument _document = this.b.getDocument();
    String _first = JnarioIterableExtensions.<String>first(args);
    Region _regionOf = this.b.regionOf(_first);
    IHyperlink[] _detectHyperLinks = this.hyperLinkDetector.detectHyperLinks(_document, _regionOf);
    this.hyperlinks = ((List<IHyperlink>)Conversions.doWrapArray(_detectHyperLinks));
  }
  
  @Test
  @Order(4)
  @Named("Then it opens an editor for \\\"test_metamodel.ect\\\"")
  public void thenItOpensAnEditorForTestMetamodelEct() {
    StepArguments _stepArguments = new StepArguments("test_metamodel.ect");
    final StepArguments args = _stepArguments;
    IHyperlink _head = IterableExtensions.<IHyperlink>head(this.hyperlinks);
    _head.open();
    IWorkbenchPage _activePage = Workbenches.getActivePage();
    IEditorPart _activeEditor = _activePage.getActiveEditor();
    String _title = _activeEditor==null?(String)null:_activeEditor.getTitle();
    String _first = JnarioIterableExtensions.<String>first(args);
    boolean _should_contain = Should.should_contain(_title, _first);
    Assert.assertTrue("\nExpected Workbenches::activePage.activeEditor?.title should contain args.first but"
     + "\n     Workbenches::activePage.activeEditor?.title is " + new StringDescription().appendValue(_title).toString()
     + "\n     Workbenches::activePage.activeEditor is " + new StringDescription().appendValue(_activeEditor).toString()
     + "\n     Workbenches::activePage is " + new StringDescription().appendValue(_activePage).toString()
     + "\n     args.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     args is " + new StringDescription().appendValue(args).toString() + "\n", _should_contain);
    
  }
}
