package org.rtext.lang.specs.util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;
import org.rtext.lang.specs.RTextPluginActivator;

@Data
@SuppressWarnings("all")
public class TestFileLocator {
  public static TestFileLocator getDefault() {
    TestFileLocator _testFileLocator = new TestFileLocator("backends/head");
    return _testFileLocator;
  }
  
  private final String _rootFolder;
  
  public String getRootFolder() {
    return this._rootFolder;
  }
  
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
          File _file = new File(_uRI);
          _xblockexpression = (_file);
        }
        _xifexpression = _xblockexpression;
      } else {
        String _resolve = this.resolve(relativePath);
        File _file = new File(_resolve);
        _xifexpression = _file;
      }
      return _xifexpression;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String getRoot() {
    String _absolutPath = this.absolutPath("");
    return _absolutPath;
  }
  
  public String absolutPath(final String relativePath) {
    File _file = this.file(relativePath);
    String _absolutePath = _file.getAbsolutePath();
    return _absolutePath;
  }
  
  private Path toFullPath(final String relativePath) {
    String _resolve = this.resolve(relativePath);
    Path _path = new Path(_resolve);
    return _path;
  }
  
  private String resolve(final String relativePath) {
    String _rootFolder = this.getRootFolder();
    String _plus = (_rootFolder + "/");
    String _plus_1 = (_plus + relativePath);
    return _plus_1;
  }
  
  public TestFileLocator(final String rootFolder) {
    super();
    this._rootFolder = rootFolder;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_rootFolder== null) ? 0 : _rootFolder.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestFileLocator other = (TestFileLocator) obj;
    if (_rootFolder == null) {
      if (other._rootFolder != null)
        return false;
    } else if (!_rootFolder.equals(other._rootFolder))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
