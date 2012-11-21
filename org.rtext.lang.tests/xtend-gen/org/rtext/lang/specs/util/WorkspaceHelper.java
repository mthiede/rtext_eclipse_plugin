package org.rtext.lang.specs.util;

import java.net.URI;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Before;

@SuppressWarnings("all")
public class WorkspaceHelper {
  private final IWorkspace workspace = new Function0<IWorkspace>() {
    public IWorkspace apply() {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      return _workspace;
    }
  }.apply();
  
  public IStatus createProject(final String name, final String folder2Link) {
    try {
      IStatus _xblockexpression = null;
      {
        final IProject project = this.project(name);
        String _name = project.getName();
        final IProjectDescription description = this.workspace.newProjectDescription(_name);
        URI _create = URI.create(folder2Link);
        description.setLocationURI(_create);
        CreateProjectOperation _createProjectOperation = new CreateProjectOperation(description, "");
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        IStatus _execute = _createProjectOperation.execute(_nullProgressMonitor, null);
        _xblockexpression = (_execute);
      }
      return _xblockexpression;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IProject project(final String name) {
    IWorkspaceRoot _root = this.workspace.getRoot();
    IProject _project = _root.getProject(name);
    return _project;
  }
  
  public IFile file(final String path) {
    IWorkspaceRoot _root = this.workspace.getRoot();
    Path _path = new Path(path);
    IFile _file = _root.getFile(_path);
    return _file;
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
  
  @Before
  public void cleanUpWorkspace() {
    IWorkspaceRoot _root = this.workspace.getRoot();
    IProject[] _projects = _root.getProjects();
    final Procedure1<IProject> _function = new Procedure1<IProject>() {
        public void apply(final IProject it) {
          try {
            NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
            it.delete(false, true, _nullProgressMonitor);
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception e = (Exception)_t;
              e.printStackTrace();
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      };
    IterableExtensions.<IProject>forEach(((Iterable<IProject>)Conversions.doWrapArray(_projects)), _function);
  }
}
