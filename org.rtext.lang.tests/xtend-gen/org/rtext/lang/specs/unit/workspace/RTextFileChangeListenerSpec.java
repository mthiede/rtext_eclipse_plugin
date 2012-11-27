package org.rtext.lang.specs.unit.workspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.specs.util.Jobs;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.specs.util.WorkspaceHelper;
import org.rtext.lang.workspace.RTextFileChangeListener;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("RTextFileChangeListener")
@CreateWith(value = MockInjector.class)
public class RTextFileChangeListenerSpec {
  public RTextFileChangeListener subject;
  
  @Extension
  public static WorkspaceHelper _workspaceHelper = new Function0<WorkspaceHelper>() {
    public WorkspaceHelper apply() {
      WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
      return _workspaceHelper;
    }
  }.apply();
  
  @Mock
  ConnectorProvider connectorProvider;
  
  @Mock
  Connector connector;
  
  @Before
  public void before() throws Exception {
    RTextPlugin _default = RTextPlugin.getDefault();
    _default.stopListeningForRTextFileChanges();
    RTextFileChangeListenerSpec._workspaceHelper.createProject("test");
    RTextFileChangeListener _rTextFileChangeListener = new RTextFileChangeListener(this.connectorProvider);
    this.subject = _rTextFileChangeListener;
    ConnectorConfig _any = Matchers.<ConnectorConfig>any(ConnectorConfig.class);
    Connector _get = this.connectorProvider.get(_any);
    OngoingStubbing<Connector> _when = Mockito.<Connector>when(_get);
    _when.thenReturn(this.connector);
  }
  
  @Test
  @Named("stops connectors when file is changed")
  @Order(1)
  public void _stopsConnectorsWhenFileIsChanged() throws Exception {
    this.createRTextFile();
    this.addListener();
    IFile _rtextFile = this.rtextFile();
    RTextFileChangeListenerSpec._workspaceHelper.append(_rtextFile, "\n");
    Jobs.waitForRTextJobs();
    ConnectorProvider _verify = Mockito.<ConnectorProvider>verify(this.connectorProvider);
    IFile _rtextFile_1 = this.rtextFile();
    IPath _location = _rtextFile_1.getLocation();
    String _string = _location.toString();
    _verify.dispose(_string);
  }
  
  @Test
  @Named("reconnects each config")
  @Order(2)
  public void _reconnectsEachConfig() throws Exception {
    this.createRTextFile();
    this.addListener();
    IFile _rtextFile = this.rtextFile();
    RTextFileChangeListenerSpec._workspaceHelper.append(_rtextFile, "\n");
    Jobs.waitForRTextJobs();
    VerificationMode _times = Mockito.times(2);
    Connector _verify = Mockito.<Connector>verify(this.connector, _times);
    _verify.connect();
  }
  
  @Test
  @Named("does nothing if file is created")
  @Order(3)
  public void _doesNothingIfFileIsCreated() throws Exception {
    this.addListener();
    this.createRTextFile();
    Jobs.waitForRTextJobs();
    VerificationMode _never = Mockito.never();
    ConnectorProvider _verify = Mockito.<ConnectorProvider>verify(this.connectorProvider, _never);
    String _anyString = Matchers.anyString();
    _verify.dispose(_anyString);
  }
  
  @Test
  @Named("stops connectors when file is deleted")
  @Order(4)
  public void _stopsConnectorsWhenFileIsDeleted() throws Exception {
    this.createRTextFile();
    this.addListener();
    IFile _rtextFile = this.rtextFile();
    RTextFileChangeListenerSpec._workspaceHelper.delete(_rtextFile);
    Jobs.waitForRTextJobs();
    ConnectorProvider _verify = Mockito.<ConnectorProvider>verify(this.connectorProvider);
    IFile _rtextFile_1 = this.rtextFile();
    IPath _location = _rtextFile_1.getLocation();
    String _string = _location.toString();
    _verify.dispose(_string);
  }
  
  @After
  public void after() throws Exception {
    IWorkspace _workspace = RTextFileChangeListenerSpec._workspaceHelper.getWorkspace();
    _workspace.removeResourceChangeListener(this.subject);
  }
  
  @After
  public void after2() throws Exception {
    RTextFileChangeListenerSpec._workspaceHelper.cleanUpWorkspace();
    RTextPlugin _default = RTextPlugin.getDefault();
    _default.startListeningForRTextFileChanges();
  }
  
  public void addListener() {
    RTextFileChangeListener _rTextFileChangeListener = new RTextFileChangeListener(this.connectorProvider);
    this.subject = _rTextFileChangeListener;
    IWorkspace _workspace = RTextFileChangeListenerSpec._workspaceHelper.getWorkspace();
    _workspace.addResourceChangeListener(this.subject);
  }
  
  public IFile rtextFile() {
    IFile _file = RTextFileChangeListenerSpec._workspaceHelper.file("test/.rtext");
    return _file;
  }
  
  public void createRTextFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("*.ect:");
    _builder.newLine();
    _builder.append("ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb \"*.ect\" 2>&1");
    _builder.newLine();
    _builder.append("*.ect2:");
    _builder.newLine();
    _builder.append("ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb \"*.ect2\" 2>&1");
    _builder.newLine();
    RTextFileChangeListenerSpec._workspaceHelper.writeToFile(_builder, "test/.rtext");
  }
}