package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.lib.StepArguments;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.specs.integration.ProblemMarkersFeature;
import org.rtext.lang.specs.util.BackendHelper;
import org.rtext.lang.specs.util.WorkspaceHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Valid files have no problem marker")
@SuppressWarnings("all")
public class ProblemMarkersFeatureValidFilesHaveNoProblemMarker extends ProblemMarkersFeature {
  @Extension
  @org.jnario.runner.Extension
  public WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
  
  @Extension
  @org.jnario.runner.Extension
  public BackendHelper b = new BackendHelper();
  
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
  @Named("When I load the model for \\\"test/test_metamodel_ok.ect2\\\"")
  public void _whenILoadTheModelForTestTestMetamodelOkEct2() {
    final StepArguments args = new StepArguments("test/test_metamodel_ok.ect2");
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    IPath _location = _file.getLocation();
    final ConnectorConfig config = this.b.startBackendFor(_location);
    this.b.loadModel();
  }
  
  @Test
  @Order(2)
  @Named("Then \\\"test/test_metamodel_ok.ect2\\\"  should have no error markers")
  public void _thenTestTestMetamodelOkEct2ShouldHaveNoErrorMarkers() {
    final StepArguments args = new StepArguments("test/test_metamodel_ok.ect2");
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    List<String> _findProblems = this._workspaceHelper.findProblems(_file);
    boolean _isEmpty = _findProblems.isEmpty();
    Assert.assertTrue("\nExpected args.first.file.findProblems.empty should be true but"
     + "\n     args.first.file.findProblems.empty is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isEmpty)).toString()
     + "\n     args.first.file.findProblems is " + new org.hamcrest.StringDescription().appendValue(_findProblems).toString()
     + "\n     args.first.file is " + new org.hamcrest.StringDescription().appendValue(_file).toString()
     + "\n     args.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     args is " + new org.hamcrest.StringDescription().appendValue(args).toString() + "\n", Should.<Boolean>should_be(Boolean.valueOf(_isEmpty), true));
    
  }
  
  @Test
  @Order(3)
  @Named("But \\\"test/test_metamodel_error.ect2\\\"  should have error markers")
  public void _butTestTestMetamodelErrorEct2ShouldHaveErrorMarkers() {
    final StepArguments args = new StepArguments("test/test_metamodel_error.ect2");
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    List<String> _findProblems = this._workspaceHelper.findProblems(_file);
    boolean _isEmpty = _findProblems.isEmpty();
    Assert.assertTrue("\nExpected args.first.file.findProblems.empty should be false but"
     + "\n     args.first.file.findProblems.empty is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_isEmpty)).toString()
     + "\n     args.first.file.findProblems is " + new org.hamcrest.StringDescription().appendValue(_findProblems).toString()
     + "\n     args.first.file is " + new org.hamcrest.StringDescription().appendValue(_file).toString()
     + "\n     args.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     args is " + new org.hamcrest.StringDescription().appendValue(args).toString() + "\n", Should.<Boolean>should_be(Boolean.valueOf(_isEmpty), false));
    
  }
}
