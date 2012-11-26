package org.rtext.lang.specs.integration;

import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
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
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.specs.integration.ProblemMarkersFeature;
import org.rtext.lang.specs.util.BackendHelper;
import org.rtext.lang.specs.util.Jobs;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.WorkspaceHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Valid files have no problem marker")
@SuppressWarnings("all")
public class ProblemMarkersFeatureValidFilesHaveNoProblemMarker extends ProblemMarkersFeature {
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
  @Named("When I load the model for \\\"test/test_metamodel_ok.ect2\\\"")
  public void whenILoadTheModelForTestTestMetamodelOkEct2() {
    StepArguments _stepArguments = new StepArguments("test/test_metamodel_ok.ect2");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    IPath _location = _file.getLocation();
    final ConnectorConfig config = this._backendHelper.startBackendFor(_location);
    LoadModelCallback _create = LoadModelCallback.create(config);
    this._backendHelper.executeSynchronousCommand(_create);
    Jobs.waitForRTextJobs();
  }
  
  @Test
  @Order(2)
  @Named("Then \\\"test/test_metamodel_ok.ect2\\\"  should have no error markers")
  public void thenTestTestMetamodelOkEct2ShouldHaveNoErrorMarkers() {
    StepArguments _stepArguments = new StepArguments("test/test_metamodel_ok.ect2");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    List<String> _findProblems = this._workspaceHelper.findProblems(_file);
    boolean _isEmpty = _findProblems.isEmpty();
    boolean _should_be = Should.<Boolean>should_be(Boolean.valueOf(_isEmpty), true);
    Assert.assertTrue("\nExpected args.first.file.findProblems.empty should be true but"
     + "\n     args.first.file.findProblems.empty is " + new StringDescription().appendValue(Boolean.valueOf(_isEmpty)).toString()
     + "\n     args.first.file.findProblems is " + new StringDescription().appendValue(_findProblems).toString()
     + "\n      is " + new StringDescription().appendValue(this._workspaceHelper).toString()
     + "\n     args.first.file is " + new StringDescription().appendValue(_file).toString()
     + "\n     args.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     args is " + new StringDescription().appendValue(args).toString() + "\n", _should_be);
    
  }
  
  @Test
  @Order(3)
  @Named("But \\\"test/test_metamodel_error.ect2\\\"  should have error markers")
  public void butTestTestMetamodelErrorEct2ShouldHaveErrorMarkers() {
    StepArguments _stepArguments = new StepArguments("test/test_metamodel_error.ect2");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    List<String> _findProblems = this._workspaceHelper.findProblems(_file);
    boolean _isEmpty = _findProblems.isEmpty();
    boolean _should_be = Should.<Boolean>should_be(Boolean.valueOf(_isEmpty), false);
    Assert.assertTrue("\nExpected args.first.file.findProblems.empty should be false but"
     + "\n     args.first.file.findProblems.empty is " + new StringDescription().appendValue(Boolean.valueOf(_isEmpty)).toString()
     + "\n     args.first.file.findProblems is " + new StringDescription().appendValue(_findProblems).toString()
     + "\n      is " + new StringDescription().appendValue(this._workspaceHelper).toString()
     + "\n     args.first.file is " + new StringDescription().appendValue(_file).toString()
     + "\n     args.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     args is " + new StringDescription().appendValue(args).toString() + "\n", _should_be);
    
  }
}
