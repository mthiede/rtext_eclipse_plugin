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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.xtend.lib.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.After;
import org.junit.Before;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.specs.util.ProjectInitializer;
import org.rtext.lang.specs.util.StringInputStream;

@SuppressWarnings("all")
public class WorkspaceHelper {
  @Property
  private final IWorkspace _workspace = ResourcesPlugin.getWorkspace();
  
  private final ArrayList<IProject> linkedProjects = CollectionLiterals.<IProject>newArrayList();
  
  private final ArrayList<IProject> createdProjects = CollectionLiterals.<IProject>newArrayList();
  
  public boolean createProject(final String name) {
    boolean _xblockexpression = false;
    {
      IWorkspace _workspace = this.getWorkspace();
      final IProjectDescription description = _workspace.newProjectDescription(name);
      IProject _doCreateProject = this.doCreateProject(name, description);
      _xblockexpression = this.createdProjects.add(_doCreateProject);
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
      final ProjectInitializer initializer = new ProjectInitializer();
      init.apply(initializer);
      initializer.apply(project);
      _xblockexpression = project;
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
      _xblockexpression = this.linkedProjects.add(_doCreateProject);
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
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IProject project(final String name) {
    IWorkspace _workspace = this.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    return _root.getProject(name);
  }
  
  public IFile file(final String path) {
    IWorkspace _workspace = this.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    Path _path = new Path(path);
    return _root.getFile(_path);
  }
  
  public IFile createFile(final String name, final CharSequence content) {
    return this.writeToFile(content, name);
  }
  
  public void createFolder(final String path) {
    try {
      IWorkspace _workspace = this.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      Path _path = new Path(path);
      IFolder _folder = _root.getFolder(_path);
      NullProgressMonitor _monitor = this.monitor();
      _folder.create(true, true, _monitor);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void createLinkedFolder(final String path, final String location) {
    try {
      IWorkspace _workspace = this.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      Path _path = new Path(path);
      final IFolder link = _root.getFolder(_path);
      IFile _file = this.file(location);
      final IPath locationPath = _file.getLocation();
      IWorkspace _workspace_1 = this.getWorkspace();
      IStatus _validateLinkLocation = _workspace_1.validateLinkLocation(link, locationPath);
      boolean _isOK = _validateLinkLocation.isOK();
      if (_isOK) {
        link.createLink(locationPath, IResource.NONE, null);
      } else {
        throw new UnsupportedOperationException();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IFile append(final IFile file, final CharSequence content) {
    try {
      IFile _xblockexpression = null;
      {
        String _string = content.toString();
        StringInputStream _stringInputStream = new StringInputStream(_string);
        NullProgressMonitor _monitor = this.monitor();
        file.appendContents(_stringInputStream, true, false, _monitor);
        _xblockexpression = file;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void delete(final IFile file) {
    try {
      NullProgressMonitor _monitor = this.monitor();
      file.delete(true, _monitor);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IFile writeToFile(final CharSequence sequence, final String name) {
    try {
      IFile _xblockexpression = null;
      {
        final IFile newFile = this.file(name);
        String _string = sequence.toString();
        StringInputStream _stringInputStream = new StringInputStream(_string);
        NullProgressMonitor _monitor = this.monitor();
        newFile.create(_stringInputStream, true, _monitor);
        _xblockexpression = newFile;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<String> findProblems(final IFile file) {
    try {
      List<String> _xblockexpression = null;
      {
        final int depth = IResource.DEPTH_INFINITE;
        IMarker[] _findMarkers = file.findMarkers(IMarker.PROBLEM, true, depth);
        final Function1<IMarker, String> _function = new Function1<IMarker, String>() {
          public String apply(final IMarker it) {
            return MarkerUtilities.getMessage(it);
          }
        };
        _xblockexpression = ListExtensions.<IMarker, String>map(((List<IMarker>)Conversions.doWrapArray(_findMarkers)), _function);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Before
  public void cleanUpWorkspace() throws Exception {
    RTextPlugin _default = RTextPlugin.getDefault();
    _default.stopListeningForRTextFileChanges();
    RTextPlugin _default_1 = RTextPlugin.getDefault();
    ConnectorProvider _connectorProvider = _default_1.getConnectorProvider();
    _connectorProvider.dispose();
    IWorkspace _workspace = this.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    IProject[] _projects = _root.getProjects();
    final Procedure1<IProject> _function = new Procedure1<IProject>() {
      public void apply(final IProject it) {
        IWorkspace _workspace = it.getWorkspace();
        IWorkspaceRoot _root = _workspace.getRoot();
        IPath _location = _root.getLocation();
        IPath _location_1 = it.getLocation();
        boolean _isPrefixOf = _location.isPrefixOf(_location_1);
        if (_isPrefixOf) {
          try {
            NullProgressMonitor _monitor = WorkspaceHelper.this.monitor();
            it.delete(true, true, _monitor);
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception e = (Exception)_t;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        } else {
          try {
            NullProgressMonitor _monitor_1 = WorkspaceHelper.this.monitor();
            it.delete(false, true, _monitor_1);
          } catch (final Throwable _t_1) {
            if (_t_1 instanceof Exception) {
              final Exception e_1 = (Exception)_t_1;
            } else {
              throw Exceptions.sneakyThrow(_t_1);
            }
          }
        }
      }
    };
    IterableExtensions.<IProject>forEach(((Iterable<IProject>)Conversions.doWrapArray(_projects)), _function);
  }
  
  @After
  public void restoreWorkspace() {
    RTextPlugin _default = RTextPlugin.getDefault();
    ConnectorProvider _connectorProvider = _default.getConnectorProvider();
    _connectorProvider.dispose();
    RTextPlugin _default_1 = RTextPlugin.getDefault();
    _default_1.startListeningForRTextFileChanges();
  }
  
  public NullProgressMonitor monitor() {
    return new NullProgressMonitor();
  }
  
  @Pure
  public IWorkspace getWorkspace() {
    return this._workspace;
  }
}
