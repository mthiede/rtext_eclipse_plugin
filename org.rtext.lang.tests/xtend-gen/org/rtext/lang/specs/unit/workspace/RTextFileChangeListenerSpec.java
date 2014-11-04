package org.rtext.lang.specs.unit.workspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.specs.util.Jobs;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.specs.util.WorkspaceHelper;
import org.rtext.lang.workspace.RTextFileChangeListener;

@CreateWith(MockInjector.class)
@Ignore
@Named("RTextFileChangeListener")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class RTextFileChangeListenerSpec {
  public RTextFileChangeListener subject;
  
  @Extension
  @org.jnario.runner.Extension
  public static WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
  
  @Mock
  ConnectorProvider connectorProvider;
  
  @Mock
  Connector connector;
  
  @Before
  public void before() throws Exception {
    RTextFileChangeListenerSpec._workspaceHelper.createProject("rtext_test2");
    RTextFileChangeListener _rTextFileChangeListener = new RTextFileChangeListener(this.connectorProvider);
    this.subject = _rTextFileChangeListener;
    ConnectorConfig _any = Matchers.<ConnectorConfig>any(ConnectorConfig.class);
    Connector _get = this.connectorProvider.get(_any);
    OngoingStubbing<Connector> _when = Mockito.<Connector>when(_get);
    _when.thenReturn(this.connector);
  }
  
  @Test
  @Ignore
  @Named("stops connectors when rtext file is changed [PENDING]")
  @Order(1)
  public void _stopsConnectorsWhenRtextFileIsChanged() throws Exception {
    this.createRTextFile();
    this.addListener();
    IFile _rtextFile = this.rtextFile();
    RTextFileChangeListenerSpec._workspaceHelper.append(_rtextFile, "\n");
    Jobs.waitForRTextJobs();
    VerificationMode _times = Mockito.times(2);
    Connector _verify = Mockito.<Connector>verify(this.connector, _times);
    _verify.disconnect();
  }
  
  @Test
  @Ignore
  @Named("reconnects each config [PENDING]")
  @Order(2)
  public void _reconnectsEachConfig() throws Exception {
    this.createRTextFile();
    this.addListener();
    IFile _rtextFile = this.rtextFile();
    RTextFileChangeListenerSpec._workspaceHelper.append(_rtextFile, "\n");
    Jobs.waitForRTextJobs();
    VerificationMode _times = Mockito.times(2);
    Connector _verify = Mockito.<Connector>verify(this.connector, _times);
    LoadModelCommand _isA = Matchers.<LoadModelCommand>isA(LoadModelCommand.class);
    Callback<LoadedModel> _any = Matchers.<Callback<LoadedModel>>any();
    _verify.<LoadedModel>execute(_isA, _any);
  }
  
  @Test
  @Ignore
  @Named("does nothing if file is created [PENDING]")
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
  
  public void addListener() {
    RTextFileChangeListener _rTextFileChangeListener = new RTextFileChangeListener(this.connectorProvider);
    this.subject = _rTextFileChangeListener;
    IWorkspace _workspace = RTextFileChangeListenerSpec._workspaceHelper.getWorkspace();
    _workspace.addResourceChangeListener(this.subject);
  }
  
  public IFile rtextFile() {
    return RTextFileChangeListenerSpec._workspaceHelper.file("rtext_test2/.rtext");
  }
  
  public IFile createRTextFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("*.ect:");
    _builder.newLine();
    _builder.append("ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb \"*.ect\" 2>&1");
    _builder.newLine();
    _builder.append("*.ect2:");
    _builder.newLine();
    _builder.append("ruby -I../../../../rgen/lib -I ../../../lib ../ecore_editor.rb \"*.ect2\" 2>&1");
    _builder.newLine();
    return RTextFileChangeListenerSpec._workspaceHelper.writeToFile(_builder, "rtext_test2/.rtext");
  }
  
  @After
  public void after() throws Exception {
    RTextFileChangeListenerSpec._workspaceHelper.cleanUpWorkspace();
    IWorkspace _workspace = RTextFileChangeListenerSpec._workspaceHelper.getWorkspace();
    _workspace.removeResourceChangeListener(this.subject);
  }
}
