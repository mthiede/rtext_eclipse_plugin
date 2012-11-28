package org.rtext.lang.specs.unit.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.specs.util.ProjectInitializer;
import org.rtext.lang.specs.util.WorkspaceHelper;
import org.rtext.lang.util.Procedure;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("ConnectorScope")
public class ConnectorScopeSpec {
  @Extension
  public WorkspaceHelper _workspaceHelper = new Function0<WorkspaceHelper>() {
    public WorkspaceHelper apply() {
      WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
      return _workspaceHelper;
    }
  }.apply();
  
  IProject project;
  
  @Test
  @Named("Contains files in same directory")
  @Order(1)
  public void _containsFilesInSameDirectory() throws Exception {
    final Procedure1<ProjectInitializer> _function = new Procedure1<ProjectInitializer>() {
        public void apply(final ProjectInitializer it) {
          it.file("file1.aaa", "content");
          it.file("file2.bbb", "content");
        }
      };
    IProject _createProject = this._workspaceHelper.createProject("test", _function);
    this.project = _createProject;
    ArrayList<String> _scope = this.scope(".rtext");
    List<String> _list = JnarioCollectionLiterals.<String>list("file1.aaa");
    boolean _doubleArrow = Should.operator_doubleArrow(_scope, _list);
    Assert.assertTrue("\nExpected scope(\".rtext\") => list(\"file1.aaa\") but"
     + "\n     scope(\".rtext\") is " + new StringDescription().appendValue(_scope).toString()
     + "\n     list(\"file1.aaa\") is " + new StringDescription().appendValue(_list).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("Contains files in subdirectory directory")
  @Order(2)
  public void _containsFilesInSubdirectoryDirectory() throws Exception {
    final Procedure1<ProjectInitializer> _function = new Procedure1<ProjectInitializer>() {
        public void apply(final ProjectInitializer it) {
          it.folder("folder");
          it.file("folder/file1.aaa", "content");
          it.file("folder/file2.bbb", "content");
        }
      };
    IProject _createProject = this._workspaceHelper.createProject("test", _function);
    this.project = _createProject;
    ArrayList<String> _scope = this.scope(".rtext");
    List<String> _list = JnarioCollectionLiterals.<String>list("file1.aaa");
    boolean _doubleArrow = Should.operator_doubleArrow(_scope, _list);
    Assert.assertTrue("\nExpected scope(\".rtext\") => list(\"file1.aaa\") but"
     + "\n     scope(\".rtext\") is " + new StringDescription().appendValue(_scope).toString()
     + "\n     list(\"file1.aaa\") is " + new StringDescription().appendValue(_list).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("Contains files in project")
  @Order(3)
  public void _containsFilesInProject() throws Exception {
    final Procedure1<ProjectInitializer> _function = new Procedure1<ProjectInitializer>() {
        public void apply(final ProjectInitializer it) {
          it.file("file1.aaa", "content");
          it.file("file2.bbb", "content");
        }
      };
    IProject _createProject = this._workspaceHelper.createProject("test", _function);
    this.project = _createProject;
    ArrayList<String> _scope = this.scope("../../.rtext");
    List<String> _list = JnarioCollectionLiterals.<String>list("file1.aaa");
    boolean _doubleArrow = Should.operator_doubleArrow(_scope, _list);
    Assert.assertTrue("\nExpected scope(\"../../.rtext\") => list(\"file1.aaa\") but"
     + "\n     scope(\"../../.rtext\") is " + new StringDescription().appendValue(_scope).toString()
     + "\n     list(\"file1.aaa\") is " + new StringDescription().appendValue(_list).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("Contains files in multiple projects")
  @Order(4)
  public void _containsFilesInMultipleProjects() throws Exception {
    final Procedure1<ProjectInitializer> _function = new Procedure1<ProjectInitializer>() {
        public void apply(final ProjectInitializer it) {
          it.file("file1.aaa", "content");
          it.file("file2.bbb", "content");
        }
      };
    IProject _createProject = this._workspaceHelper.createProject("test1", _function);
    this.project = _createProject;
    final Procedure1<ProjectInitializer> _function_1 = new Procedure1<ProjectInitializer>() {
        public void apply(final ProjectInitializer it) {
          it.file("file3.aaa", "content");
          it.file("file4.bbb", "content");
        }
      };
    this._workspaceHelper.createProject("test2", _function_1);
    ArrayList<String> _scope = this.scope("../../.rtext");
    List<String> _list = JnarioCollectionLiterals.<String>list("file1.aaa", "file3.aaa");
    boolean _doubleArrow = Should.operator_doubleArrow(_scope, _list);
    Assert.assertTrue("\nExpected scope(\"../../.rtext\") => list(\"file1.aaa\", \"file3.aaa\") but"
     + "\n     scope(\"../../.rtext\") is " + new StringDescription().appendValue(_scope).toString()
     + "\n     list(\"file1.aaa\", \"file3.aaa\") is " + new StringDescription().appendValue(_list).toString() + "\n", _doubleArrow);
    
  }
  
  public ArrayList<String> scope(final String path) {
    ArrayList<String> _xblockexpression = null;
    {
      IPath _location = this.project.getLocation();
      File location = _location.toFile();
      String _string = location.toString();
      String _plus = (_string + "/");
      String _plus_1 = (_plus + path);
      File _file = new File(_plus_1);
      location = _file;
      ConnectorConfig _connectorConfig = new ConnectorConfig(location, "", "*.aaa");
      final ConnectorScope scope = ConnectorScope.create(_connectorConfig);
      final ArrayList<String> scopedElements = CollectionLiterals.<String>newArrayList();
      final Procedure1<IResource> _function = new Procedure1<IResource>() {
          public void apply(final IResource it) {
            String _name = it.getName();
            scopedElements.add(_name);
          }
        };
      scope.forEach(new Procedure<IResource>() {
          public void apply(IResource param) {
            _function.apply(param);
          }
      });
      _xblockexpression = (scopedElements);
    }
    return _xblockexpression;
  }
}
