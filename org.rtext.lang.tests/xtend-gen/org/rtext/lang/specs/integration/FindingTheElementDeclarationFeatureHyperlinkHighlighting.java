package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
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
    try {
      final StepArguments args = new StepArguments("/StatemachineMM/State");
      IHyperlink _head = IterableExtensions.<IHyperlink>head(this.hyperlinks);
      final IRegion region = _head.getHyperlinkRegion();
      int _offset = region.getOffset();
      int _length = region.getLength();
      String _get = this.b.document.get(_offset, _length);
      String _first = JnarioIterableExtensions.<String>first(args);
      Assert.assertTrue("\nExpected b.document.get(region.offset, region.length) => args.first but"
       + "\n     b.document.get(region.offset, region.length) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
       + "\n     b.document is " + new org.hamcrest.StringDescription().appendValue(this.b.document).toString()
       + "\n     b is " + new org.hamcrest.StringDescription().appendValue(this.b).toString()
       + "\n     region.offset is " + new org.hamcrest.StringDescription().appendValue(_offset).toString()
       + "\n     region is " + new org.hamcrest.StringDescription().appendValue(region).toString()
       + "\n     region.length is " + new org.hamcrest.StringDescription().appendValue(_length).toString()
       + "\n     args.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
       + "\n     args is " + new org.hamcrest.StringDescription().appendValue(args).toString() + "\n", Should.<String>operator_doubleArrow(_get, _first));
      
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
