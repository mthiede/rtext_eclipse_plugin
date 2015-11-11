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
@Named("Scenario: Disconnected backend")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureDisconnectedBackend extends FindingTheElementDeclarationFeatureBackground {
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
  @Named("Given the backend is disconnected")
  public void _givenTheBackendIsDisconnected() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field b is undefined for the type Scenario: Disconnected backend\n");
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
  @Named("Then the hyperlink message is \\\"model not yet loaded\\\"")
  public void _thenTheHyperlinkMessageIsModelNotYetLoaded() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field hyperlinks is undefined for the type Scenario: Disconnected backend\n"
      + "\nAmbiguous feature call.\nThe extension methods\n\t<T> first(Iterable<T>) in JnarioIterableExtensions and\n\t<T> first(Iterator<T>) in JnarioIteratorExtensions\nboth match.");
  }
}
