package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureBackground;

@RunWith(FeatureRunner.class)
@Named("Scenario: Hyperlink highlighting")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureHyperlinkHighlighting extends FindingTheElementDeclarationFeatureBackground {
  @Test
  @Order(0)
  @Named("Given a project \\\"test\\\" linked to \\\"rtext/test/integration/model/\\\"")
  public void _givenAProjectTestLinkedToRtextTestIntegrationModel() {
    super._givenAProjectTestLinkedToRtextTestIntegrationModel();
  }
  
  @Test
  @Order(1)
  @Named("And a backend for \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void _andABackendForRtextTestIntegrationModelTestMetamodelEct() {
    super._andABackendForRtextTestIntegrationModelTestMetamodelEct();
  }
  
  @Test
  @Order(2)
  @Named("And a hyperlink detector")
  public void _andAHyperlinkDetector() {
    super._andAHyperlinkDetector();
  }
  
  @Test
  @Order(3)
  @Named("Given the model is loaded")
  public void _givenTheModelIsLoaded() {
    this.b.loadModel();
  }
  
  @Test
  @Order(4)
  @Named("When I get the hyperlinks for \\\"/StatemachineMM/State\\\"")
  public void _whenIGetTheHyperlinksForStatemachineMMState() {
    final StepArguments args = new StepArguments("/StatemachineMM/State");
    String _first = JnarioIterableExtensions.<String>first(args);
    Region _regionOf = this.b.regionOf(_first);
    IHyperlink[] _detectHyperLinks = this.hyperLinkDetector.detectHyperLinks(this.b.document, _regionOf);
    this.hyperlinks = ((List<IHyperlink>)Conversions.doWrapArray(_detectHyperLinks));
  }
  
  @Test
  @Order(5)
  @Named("Then the hyperlink text is \\\"/StatemachineMM/State\\\"")
  public void _thenTheHyperlinkTextIsStatemachineMMState() {
  }
}
