package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.editor.HyperlinkDetector;
import org.rtext.lang.proposals.ContentAssistProcessor;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeature;
import org.rtext.lang.specs.util.BackendHelper;
import org.rtext.lang.specs.util.WorkspaceHelper;

@RunWith(FeatureRunner.class)
@Named("Background:")
@SuppressWarnings("all")
public abstract class FindingTheElementDeclarationFeatureBackground extends FindingTheElementDeclarationFeature {
  HyperlinkDetector hyperLinkDetector;
  
  List<IHyperlink> hyperlinks;
  
  @Extension
  @org.jnario.runner.Extension
  public WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
  
  @Extension
  @org.jnario.runner.Extension
  public BackendHelper b = new BackendHelper();
  
  String modelFile;
  
  List<String> proposals;
  
  ContentAssistProcessor proposalProvider;
  
  @Test
  @Order(0)
  @Named("Given a project \\\"test\\\" linked to \\\"rtext/test/integration/model/\\\"")
  public void _givenAProjectTestLinkedToRtextTestIntegrationModel() {
    final StepArguments args = new StepArguments("test", "rtext/test/integration/model/");
    String _first = JnarioIterableExtensions.<String>first(args);
    String _second = JnarioIterableExtensions.<String>second(args);
    String _absolutPath = this.b.absolutPath(_second);
    this._workspaceHelper.createProject(_first, _absolutPath);
  }
  
  @Test
  @Order(1)
  @Named("And a backend for \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void _andABackendForRtextTestIntegrationModelTestMetamodelEct() {
    final StepArguments args = new StepArguments("rtext/test/integration/model/test_metamodel.ect");
    String _first = JnarioIterableExtensions.<String>first(args);
    String _absolutPath = this.b.absolutPath(_first);
    this.b.startBackendFor(_absolutPath);
  }
  
  @Test
  @Order(2)
  @Named("And a hyperlink detector")
  public void _andAHyperlinkDetector() {
    IHyperlinkDetector _create = HyperlinkDetector.create(this.b);
    this.hyperLinkDetector = ((HyperlinkDetector) _create);
  }
}
