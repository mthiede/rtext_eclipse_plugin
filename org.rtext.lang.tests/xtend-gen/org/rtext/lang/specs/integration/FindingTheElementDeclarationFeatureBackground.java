package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.Extension;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
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

@RunWith(FeatureRunner.class)
@Named("Background:")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureBackground extends FindingTheElementDeclarationFeature {
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
          return FindingTheElementDeclarationFeatureBackground.this.b.getConnector();
        }
      };
    HyperlinkDetector _hyperlinkDetector = new HyperlinkDetector(new Connected() {
        public Connector getConnector() {
          return _function.apply();
        }
    });
    this.hyperLinkDetector = _hyperlinkDetector;
  }
}
