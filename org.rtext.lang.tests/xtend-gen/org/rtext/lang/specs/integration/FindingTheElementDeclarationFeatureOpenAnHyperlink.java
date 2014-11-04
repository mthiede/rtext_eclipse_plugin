package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.xtext.xbase.lib.Conversions;
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
import org.rtext.lang.util.Workbenches;

@RunWith(FeatureRunner.class)
@Named("Scenario: Open an hyperlink")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeatureOpenAnHyperlink extends FindingTheElementDeclarationFeatureBackground {
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
  @Named("Then it opens an editor for \\\"test_metamodel.ect\\\"")
  public void _thenItOpensAnEditorForTestMetamodelEct() {
    final StepArguments args = new StepArguments("test_metamodel.ect");
    IHyperlink _head = IterableExtensions.<IHyperlink>head(this.hyperlinks);
    _head.open();
    IWorkbenchPage _activePage = Workbenches.getActivePage();
    IEditorPart _activeEditor = _activePage.getActiveEditor();
    String _title = null;
    if (_activeEditor!=null) {
      _title=_activeEditor.getTitle();
    }
    String _first = JnarioIterableExtensions.<String>first(args);
    Assert.assertTrue("\nExpected Workbenches::activePage.activeEditor?.title should contain args.first but"
     + "\n     Workbenches::activePage.activeEditor?.title is " + new org.hamcrest.StringDescription().appendValue(_title).toString()
     + "\n     Workbenches::activePage.activeEditor is " + new org.hamcrest.StringDescription().appendValue(_activeEditor).toString()
     + "\n     Workbenches::activePage is " + new org.hamcrest.StringDescription().appendValue(_activePage).toString()
     + "\n     Workbenches is " + new org.hamcrest.StringDescription().appendValue(Workbenches.class).toString()
     + "\n     args.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     args is " + new org.hamcrest.StringDescription().appendValue(args).toString() + "\n", Should.<Object>should_contain(_title, _first));
    
  }
}
