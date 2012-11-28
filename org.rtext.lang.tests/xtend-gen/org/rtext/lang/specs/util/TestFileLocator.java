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
  
  private final String _root;
  
  public String getRoot() {
    return this._root;
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
    String _root = this.getRoot();
    String _plus = (_root + "/");
    String _plus_1 = (_plus + relativePath);
    return _plus_1;
  }
  
  public TestFileLocator(final String root) {
    super();
    this._root = root;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_root== null) ? 0 : _root.hashCode());
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
    if (_root == null) {
      if (other._root != null)
        return false;
    } else if (!_root.equals(other._root))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
