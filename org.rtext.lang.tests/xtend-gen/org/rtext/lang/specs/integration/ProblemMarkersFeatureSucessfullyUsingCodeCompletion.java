package org.rtext.lang.specs.integration;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.StepArguments;
import org.jnario.runner.Extension;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.editor.LoadModelCallback;
import org.rtext.lang.specs.integration.ProblemMarkersFeature;
import org.rtext.lang.specs.util.BackendHelper;
import org.rtext.lang.specs.util.TestFileLocator;
import org.rtext.lang.specs.util.WorkspaceHelper;

@RunWith(FeatureRunner.class)
@Named("Scenario: Sucessfully using code completion")
@SuppressWarnings("all")
public class ProblemMarkersFeatureSucessfullyUsingCodeCompletion extends ProblemMarkersFeature {
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
  @Named("When I load the model for \\\"test/test_metamodel.ect\\\"")
  public void whenILoadTheModelForTestTestMetamodelEct() {
    StepArguments _stepArguments = new StepArguments("test/test_metamodel.ect");
    final StepArguments args = _stepArguments;
    String _first = JnarioIterableExtensions.<String>first(args);
    IFile _file = this._workspaceHelper.file(_first);
    IPath _location = _file.getLocation();
    this._backendHelper.startBackendFor(_location);
    LoadModelCallback _create = LoadModelCallback.create();
    this._backendHelper.executeSynchronousCommand(_create);
  }
  
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
}