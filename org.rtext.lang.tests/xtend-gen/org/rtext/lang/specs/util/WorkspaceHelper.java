package org.rtext.lang.specs.util;

import java.net.URI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

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
}
