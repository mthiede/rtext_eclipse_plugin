package org.rtext.lang.specs.unit.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorScope;
import org.rtext.lang.specs.util.ProjectInitializer;
import org.rtext.lang.specs.util.WorkspaceHelper;
import org.rtext.lang.util.Procedure;

@Named("ConnectorScope")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ConnectorScopeSpec {
  @Extension
  @org.jnario.runner.Extension
  public WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
  
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
    IProject _createProject = this._workspaceHelper.createProject("scope_test", _function);
    this.project = _createProject;
    Set<String> _scope = this.scope(".rtext");
    Set<String> _set = JnarioCollectionLiterals.<String>set("/scope_test/file1.aaa");
    Assert.assertTrue("\nExpected scope(\".rtext\") => set(\"/scope_test/file1.aaa\") but"
     + "\n     scope(\".rtext\") is " + new org.hamcrest.StringDescription().appendValue(_scope).toString()
     + "\n     set(\"/scope_test/file1.aaa\") is " + new org.hamcrest.StringDescription().appendValue(_set).toString() + "\n", Should.<Set<String>>operator_doubleArrow(_scope, _set));
    
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
    IProject _createProject = this._workspaceHelper.createProject("scope_test", _function);
    this.project = _createProject;
    Set<String> _scope = this.scope(".rtext");
    Set<String> _set = JnarioCollectionLiterals.<String>set("/scope_test/folder/file1.aaa");
    Assert.assertTrue("\nExpected scope(\".rtext\") => set(\"/scope_test/folder/file1.aaa\") but"
     + "\n     scope(\".rtext\") is " + new org.hamcrest.StringDescription().appendValue(_scope).toString()
     + "\n     set(\"/scope_test/folder/file1.aaa\") is " + new org.hamcrest.StringDescription().appendValue(_set).toString() + "\n", Should.<Set<String>>operator_doubleArrow(_scope, _set));
    
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
    IProject _createProject = this._workspaceHelper.createProject("scope_test", _function);
    this.project = _createProject;
    Set<String> _scope = this.scope("../../.rtext");
    Set<String> _set = JnarioCollectionLiterals.<String>set("/scope_test/file1.aaa");
    Assert.assertTrue("\nExpected scope(\"../../.rtext\") => set(\"/scope_test/file1.aaa\") but"
     + "\n     scope(\"../../.rtext\") is " + new org.hamcrest.StringDescription().appendValue(_scope).toString()
     + "\n     set(\"/scope_test/file1.aaa\") is " + new org.hamcrest.StringDescription().appendValue(_set).toString() + "\n", Should.<Set<String>>operator_doubleArrow(_scope, _set));
    
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
    IProject _createProject = this._workspaceHelper.createProject("scope_test1", _function);
    this.project = _createProject;
    final Procedure1<ProjectInitializer> _function_1 = new Procedure1<ProjectInitializer>() {
      public void apply(final ProjectInitializer it) {
        it.file("file3.aaa", "content");
        it.file("file4.bbb", "content");
      }
    };
    this._workspaceHelper.createProject("scope_test2", _function_1);
    Set<String> _scope = this.scope("../../.rtext");
    Set<String> _set = JnarioCollectionLiterals.<String>set("/scope_test1/file1.aaa", "/scope_test2/file3.aaa");
    Assert.assertTrue("\nExpected scope(\"../../.rtext\") => set(\"/scope_test1/file1.aaa\", \"/scope_test2/file3.aaa\") but"
     + "\n     scope(\"../../.rtext\") is " + new org.hamcrest.StringDescription().appendValue(_scope).toString()
     + "\n     set(\"/scope_test1/file1.aaa\", \"/scope_test2/file3.aaa\") is " + new org.hamcrest.StringDescription().appendValue(_set).toString() + "\n", Should.<Set<String>>operator_doubleArrow(_scope, _set));
    
  }
  
  @Test
  @Named("Contains linked files in multiple projects")
  @Order(5)
  public void _containsLinkedFilesInMultipleProjects() throws Exception {
    final Procedure1<ProjectInitializer> _function = new Procedure1<ProjectInitializer>() {
      public void apply(final ProjectInitializer it) {
        it.folder("folder");
        it.file("folder/file1.aaa", "content");
      }
    };
    IProject _createProject = this._workspaceHelper.createProject("scope_test1", _function);
    this.project = _createProject;
    final Procedure1<ProjectInitializer> _function_1 = new Procedure1<ProjectInitializer>() {
      public void apply(final ProjectInitializer it) {
        it.linkedFolder("folder", "scope_test1/folder");
      }
    };
    this._workspaceHelper.createProject("scope_test2", _function_1);
    Set<String> _scope = this.scope("../../.rtext");
    Set<String> _set = JnarioCollectionLiterals.<String>set("/scope_test1/folder/file1.aaa", "/scope_test2/folder/file1.aaa");
    Assert.assertTrue("\nExpected scope(\"../../.rtext\") => set(\"/scope_test1/folder/file1.aaa\", \"/scope_test2/folder/file1.aaa\") but"
     + "\n     scope(\"../../.rtext\") is " + new org.hamcrest.StringDescription().appendValue(_scope).toString()
     + "\n     set(\"/scope_test1/folder/file1.aaa\", \"/scope_test2/folder/file1.aaa\") is " + new org.hamcrest.StringDescription().appendValue(_set).toString() + "\n", Should.<Set<String>>operator_doubleArrow(_scope, _set));
    
  }
  
  public Set<String> scope(final String path) {
    Set<String> _xblockexpression = null;
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
      final Procedure<IResource> _function = new Procedure<IResource>() {
        public void apply(final IResource it) {
          IPath _fullPath = it.getFullPath();
          String _string = _fullPath.toString();
          scopedElements.add(_string);
        }
      };
      scope.forEach(_function);
      _xblockexpression = IterableExtensions.<String>toSet(scopedElements);
    }
    return _xblockexpression;
  }
}
