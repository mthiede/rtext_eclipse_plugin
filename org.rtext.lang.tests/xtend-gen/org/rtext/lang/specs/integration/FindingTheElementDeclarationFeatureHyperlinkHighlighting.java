package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureBackground;

@RunWith(FeatureRunner.class)
@Named("Scenario: Hyperlink highlighting")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureHyperlinkHighlighting extends FindingTheElementDeclarationFeatureBackground {
  @Test
  @Order(0)
  @Named("Given a project \\\"test\\\" linked to \\\"rtext/test/integration/model/\\\"")
  public void givenAProjectTestLinkedToRtextTestIntegrationModel() {
    super.givenAProjectTestLinkedToRtextTestIntegrationModel();
  }
  
  @Test
  @Order(1)
  @Named("And a backend for \\\"rtext/test/integration/model/test_metamodel.ect\\\"")
  public void andABackendForRtextTestIntegrationModelTestMetamodelEct() {
    super.andABackendForRtextTestIntegrationModelTestMetamodelEct();
  }
  
  @Test
  @Order(2)
  @Named("And a hyperlink detector")
  public void andAHyperlinkDetector() {
    super.andAHyperlinkDetector();
  }
  
  @Test
  @Order(3)
  @Named("When I get the hyperlinks for \\\"/StatemachineMM/State\\\"")
  public void whenIGetTheHyperlinksForStatemachineMMState() {
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
  @Named("Then the hyperlink text is \\\"/StatemachineMM/State\\\"")
  public void thenTheHyperlinkTextIsStatemachineMMState() {
    try {
      StepArguments _stepArguments = new StepArguments("/StatemachineMM/State");
      final StepArguments args = _stepArguments;
      IHyperlink _head = IterableExtensions.<IHyperlink>head(this.hyperlinks);
      final IRegion region = _head.getHyperlinkRegion();
      IDocument _document = this.b.getDocument();
      int _offset = region.getOffset();
      int _length = region.getLength();
      String _get = _document.get(_offset, _length);
      String _first = JnarioIterableExtensions.<String>first(args);
      boolean _doubleArrow = Should.operator_doubleArrow(_get, _first);
      Assert.assertTrue("\nExpected b.document.get(region.offset, region.length) => args.first but"
       + "\n     b.document.get(region.offset, region.length) is " + new StringDescription().appendValue(_get).toString()
       + "\n     b.document is " + new StringDescription().appendValue(_document).toString()
       + "\n     b is " + new StringDescription().appendValue(this.b).toString()
       + "\n     region.offset is " + new StringDescription().appendValue(_offset).toString()
       + "\n     region is " + new StringDescription().appendValue(region).toString()
       + "\n     region.length is " + new StringDescription().appendValue(_length).toString()
       + "\n     args.first is " + new StringDescription().appendValue(_first).toString()
       + "\n     args is " + new StringDescription().appendValue(args).toString() + "\n", _doubleArrow);
      
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
