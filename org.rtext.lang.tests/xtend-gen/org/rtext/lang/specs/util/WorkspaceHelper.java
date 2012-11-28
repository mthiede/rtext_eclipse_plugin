package org.rtext.lang.specs.util;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.After;

@SuppressWarnings("all")
public class WorkspaceHelper {
  private final IWorkspace _workspace = new Function0<IWorkspace>() {
    public IWorkspace apply() {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      return _workspace;
    }
  }.apply();
  
  public IWorkspace getWorkspace() {
    return this._workspace;
  }
  
  private final ArrayList<IProject> linkedProjects = new Function0<ArrayList<IProject>>() {
    public ArrayList<IProject> apply() {
      ArrayList<IProject> _newArrayList = CollectionLiterals.<IProject>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private final ArrayList<IProject> createdProjects = new Function0<ArrayList<IProject>>() {
    public ArrayList<IProject> apply() {
      ArrayList<IProject> _newArrayList = CollectionLiterals.<IProject>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public boolean createProject(final String name) {
    boolean _xblockexpression = false;
    {
      IWorkspace _workspace = this.getWorkspace();
      final IProjectDescription description = _workspace.newProjectDescription(name);
      IProject _doCreateProject = this.doCreateProject(name, description);
      boolean _add = this.createdProjects.add(_doCreateProject);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  public IProject createProject(final String name, final Procedure1<ProjectInitializer> init) {
    IProject _xblockexpression = null;
    {
      IWorkspace _workspace = this.getWorkspace();
      final IProjectDescription description = _workspace.newProjectDescription(name);
      final IProject project = this.doCreateProject(name, description);
      this.createdProjects.add(project);
      ProjectInitializer _projectInitializer = new ProjectInitializer();
      final ProjectInitializer initializer = _projectInitializer;
      init.apply(initializer);
      initializer.apply(project);
      _xblockexpression = (project);
    }
    return _xblockexpression;
  }
  
  public boolean createProject(final String name, final String folder2Link) {
    boolean _xblockexpression = false;
    {
      IWorkspace _workspace = this.getWorkspace();
      final IProjectDescription description = _workspace.newProjectDescription(name);
      File _file = new File(folder2Link);
      URI _uRI = _file.toURI();
      description.setLocationURI(_uRI);
      IProject _doCreateProject = this.doCreateProject(name, description);
      boolean _add = this.linkedProjects.add(_doCreateProject);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  private IProject doCreateProject(final String name, final IProjectDescription description) {
    try {
      IWorkspace _workspace = this.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      final IProject project = _root.getProject(name);
      NullProgressMonitor _monitor = this.monitor();
      project.create(description, _monitor);
      NullProgressMonitor _monitor_1 = this.monitor();
      project.open(_monitor_1);
      return project;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IProject project(final String name) {
    IWorkspace _workspace = this.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    IProject _project = _root.getProject(name);
    return _project;
  }
  
  public IFile file(final String path) {
    IWorkspace _workspace = this.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    Path _path = new Path(path);
    IFile _file = _root.getFile(_path);
    return _file;
  }
  
  public void createFolder(final String path) {
    try {
      IWorkspace _workspace = this.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      Path _path = new Path(path);
      IFolder _folder = _root.getFolder(_path);
      NullProgressMonitor _monitor = this.monitor();
      _folder.create(true, true, _monitor);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void append(final IFile file, final CharSequence content) {
    try {
      String _string = content.toString();
      StringInputStream _stringInputStream = new StringInputStream(_string);
      NullProgressMonitor _monitor = this.monitor();
      file.appendContents(_stringInputStream, true, false, _monitor);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void delete(final IFile file) {
    try {
      NullProgressMonitor _monitor = this.monitor();
      file.delete(true, _monitor);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void writeToFile(final CharSequence sequence, final String name) {
    try {
      IFile _file = this.file(name);
      String _string = sequence.toString();
      StringInputStream _stringInputStream = new StringInputStream(_string);
      NullProgressMonitor _monitor = this.monitor();
      _file.create(_stringInputStream, true, _monitor);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<String> findProblems(final IFile file) {
    try {
      List<String> _xblockexpression = null;
      {
        final int depth = IResource.DEPTH_INFINITE;
        IMarker[] _findMarkers = file.findMarkers(IMarker.PROBLEM, true, depth);
        final Function1<IMarker,String> _function = new Function1<IMarker,String>() {
            public String apply(final IMarker it) {
              String _message = MarkerUtilities.getMessage(it);
              return _message;
            }
          };
        List<String> _map = ListExtensions.<IMarker, String>map(((List<IMarker>)Conversions.doWrapArray(_findMarkers)), _function);
        _xblockexpression = (_map);
      }
      return _xblockexpression;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @After
  public void cleanUpWorkspace() throws Exception {
    final Procedure1<IProject> _function = new Procedure1<IProject>() {
        public void apply(final IProject it) {
          try {
            NullProgressMonitor _monitor = WorkspaceHelper.this.monitor();
            it.delete(false, true, _monitor);
          } catch (Exception _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
    IterableExtensions.<IProject>forEach(this.linkedProjects, _function);
    final Procedure1<IProject> _function_1 = new Procedure1<IProject>() {
        public void apply(final IProject it) {
          try {
            NullProgressMonitor _monitor = WorkspaceHelper.this.monitor();
            it.delete(true, true, _monitor);
          } catch (Exception _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
    IterableExtensions.<IProject>forEach(this.createdProjects, _function_1);
  }
  
  public NullProgressMonitor monitor() {
    NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
    return _nullProgressMonitor;
  }
}
