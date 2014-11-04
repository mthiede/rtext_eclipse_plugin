package org.rtext.lang.specs.util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;
import org.rtext.lang.specs.RTextPluginActivator;

@Data
@SuppressWarnings("all")
public class TestFileLocator {
  public static TestFileLocator getDefault() {
    return new TestFileLocator("backends/head");
  }
  
  private final String _rootFolder;
  
  private File file(final String relativePath) {
    try {
      File _xifexpression = null;
      boolean _isRunning = Platform.isRunning();
      if (_isRunning) {
        File _xblockexpression = null;
        {
          final Path fullpath = this.toFullPath(relativePath);
          RTextPluginActivator _default = RTextPluginActivator.getDefault();
          final URL url = _default.find(fullpath);
          final URL fileUrl = FileLocator.toFileURL(url);
          URI _uRI = fileUrl.toURI();
          _xblockexpression = new File(_uRI);
        }
        _xifexpression = _xblockexpression;
      } else {
        String _resolve = this.resolve(relativePath);
        _xifexpression = new File(_resolve);
      }
      return _xifexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String getRoot() {
    return this.absolutPath("");
  }
  
  public String absolutPath(final String relativePath) {
    File _file = this.file(relativePath);
    return _file.getAbsolutePath();
  }
  
  private Path toFullPath(final String relativePath) {
    String _resolve = this.resolve(relativePath);
    return new Path(_resolve);
  }
  
  private String resolve(final String relativePath) {
    String _rootFolder = this.getRootFolder();
    String _plus = (_rootFolder + "/");
    return (_plus + relativePath);
  }
  
  public TestFileLocator(final String rootFolder) {
    super();
    this._rootFolder = rootFolder;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._rootFolder== null) ? 0 : this._rootFolder.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestFileLocator other = (TestFileLocator) obj;
    if (this._rootFolder == null) {
      if (other._rootFolder != null)
        return false;
    } else if (!this._rootFolder.equals(other._rootFolder))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
  
  @Pure
  public String getRootFolder() {
    return this._rootFolder;
  }
}
