package org.rtext.lang.specs.unit.backend;

import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.Matcher;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.specs.util.WorkspaceHelper;
import org.rtext.lang.util.FileLocator;

@CreateWith(MockInjector.class)
@Named("LoadModelCallback")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class LoadModelCallbackSpec {
  public LoadModelCallback subject;
  
  @Extension
  @org.jnario.runner.Extension
  public WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
  
  @Mock
  LoadModelCallback.ProblemUpdateJobFactory jobFactory;
  
  @Mock
  ConnectorScope connectorScope;
  
  @Mock
  FileLocator fileLocator;
  
  @Mock
  LoadModelCallback.ProblemUpdateJob updateJob;
  
  @Mock
  IFile resolvedFile1;
  
  @Mock
  IFile resolvedFile2;
  
  final List<LoadedModel.Problem> problems1 = JnarioCollectionLiterals.<LoadedModel.Problem>list(
    new LoadedModel.Problem("message1", "error", 1), 
    new LoadedModel.Problem("message2", "warning", 2));
  
  final List<LoadedModel.Problem> problems2 = JnarioCollectionLiterals.<LoadedModel.Problem>list(
    new LoadedModel.Problem("message3", "error", 1), 
    new LoadedModel.Problem("message4", "warning", 2));
  
  final LoadedModel loadedModel = ObjectExtensions.<LoadedModel>operator_doubleArrow(new LoadedModel(), new Procedure1<LoadedModel>() {
    public void apply(final LoadedModel it) {
      List<LoadedModel.FileProblems> _problems = it.getProblems();
      LoadedModel.FileProblems _fileProblems = new LoadedModel.FileProblems("myfile1.txt", LoadModelCallbackSpec.this.problems1);
      _problems.add(_fileProblems);
      List<LoadedModel.FileProblems> _problems_1 = it.getProblems();
      LoadedModel.FileProblems _fileProblems_1 = new LoadedModel.FileProblems("myfile2.txt", LoadModelCallbackSpec.this.problems2);
      _problems_1.add(_fileProblems_1);
    }
  });
  
  @Before
  public void before() throws Exception {
    LoadModelCallback _loadModelCallback = new LoadModelCallback(this.jobFactory, this.fileLocator, this.connectorScope);
    this.subject = _loadModelCallback;
    Map _anyMap = Matchers.anyMap();
    ConnectorScope _any = Matchers.<ConnectorScope>any();
    LoadModelCallback.ProblemUpdateJob _create = this.jobFactory.create(_anyMap, _any);
    OngoingStubbing<LoadModelCallback.ProblemUpdateJob> _when = Mockito.<LoadModelCallback.ProblemUpdateJob>when(_create);
    _when.thenReturn(this.updateJob);
    List<IFile> _locate = this.fileLocator.locate("myfile1.txt");
    OngoingStubbing<List<IFile>> _when_1 = Mockito.<List<IFile>>when(_locate);
    List<IFile> _list = JnarioCollectionLiterals.<IFile>list(this.resolvedFile1);
    _when_1.thenReturn(_list);
    List<IFile> _locate_1 = this.fileLocator.locate("myfile2.txt");
    OngoingStubbing<List<IFile>> _when_2 = Mockito.<List<IFile>>when(_locate_1);
    List<IFile> _list_1 = JnarioCollectionLiterals.<IFile>list(this.resolvedFile2);
    _when_2.thenReturn(_list_1);
    boolean _contains = this.resolvedFile1.contains(this.resolvedFile1);
    OngoingStubbing<Boolean> _when_3 = Mockito.<Boolean>when(Boolean.valueOf(_contains));
    _when_3.thenReturn(Boolean.valueOf(true));
    boolean _contains_1 = this.resolvedFile2.contains(this.resolvedFile2);
    OngoingStubbing<Boolean> _when_4 = Mockito.<Boolean>when(Boolean.valueOf(_contains_1));
    _when_4.thenReturn(Boolean.valueOf(true));
    boolean _isConflicting = this.resolvedFile1.isConflicting(this.resolvedFile1);
    OngoingStubbing<Boolean> _when_5 = Mockito.<Boolean>when(Boolean.valueOf(_isConflicting));
    _when_5.thenReturn(Boolean.valueOf(true));
    boolean _isConflicting_1 = this.resolvedFile2.isConflicting(this.resolvedFile2);
    OngoingStubbing<Boolean> _when_6 = Mockito.<Boolean>when(Boolean.valueOf(_isConflicting_1));
    _when_6.thenReturn(Boolean.valueOf(true));
  }
  
  @Test
  @Named("creates update job on response with problems per file")
  @Order(1)
  public void _createsUpdateJobOnResponseWithProblemsPerFile() throws Exception {
    this.subject.commandSent();
    this.subject.handleResponse(this.loadedModel);
    final Function1<Map<IResource, List<LoadedModel.Problem>>, Boolean> _function = new Function1<Map<IResource, List<LoadedModel.Problem>>, Boolean>() {
      public Boolean apply(final Map<IResource, List<LoadedModel.Problem>> it) {
        boolean _and = false;
        boolean _and_1 = false;
        boolean _and_2 = false;
        boolean _containsKey = it.containsKey(LoadModelCallbackSpec.this.resolvedFile1);
        if (!_containsKey) {
          _and_2 = false;
        } else {
          boolean _containsKey_1 = it.containsKey(LoadModelCallbackSpec.this.resolvedFile2);
          _and_2 = _containsKey_1;
        }
        if (!_and_2) {
          _and_1 = false;
        } else {
          boolean _containsValue = it.containsValue(LoadModelCallbackSpec.this.problems1);
          _and_1 = _containsValue;
        }
        if (!_and_1) {
          _and = false;
        } else {
          boolean _containsValue_1 = it.containsValue(LoadModelCallbackSpec.this.problems2);
          _and = _containsValue_1;
        }
        return Boolean.valueOf(_and);
      }
    };
    final Matcher<? super Map<IResource, List<LoadedModel.Problem>>> matcher = Should.<Map<IResource, List<LoadedModel.Problem>>>matches("problems", _function);
    LoadModelCallback.ProblemUpdateJobFactory _verify = Mockito.<LoadModelCallback.ProblemUpdateJobFactory>verify(this.jobFactory);
    Object _argThat = Matchers.argThat(matcher);
    ConnectorScope _eq = Matchers.<ConnectorScope>eq(this.connectorScope);
    _verify.create(((Map<IResource, List<LoadedModel.Problem>>) _argThat), _eq);
  }
  
  @Test
  @Named("schedules update job")
  @Order(2)
  public void _schedulesUpdateJob() throws Exception {
    this.subject.commandSent();
    this.subject.handleResponse(this.loadedModel);
    LoadModelCallback.ProblemUpdateJob _verify = Mockito.<LoadModelCallback.ProblemUpdateJob>verify(this.updateJob);
    _verify.schedule();
  }
}
