package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.xtext.xbase.lib.Conversions;
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
import org.rtext.lang.util.Workbenches;

@RunWith(FeatureRunner.class)
@Named("Scenario: Find declaration within same file")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureFindDeclarationWithinSameFile extends FindingTheElementDeclarationFeatureBackground {
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
