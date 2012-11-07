package org.rtext.lang.specs.util;

import java.io.File;
import java.io.PrintWriter;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class Files {
  public static File newFile(final File folder, final String name) {
    try {
      File _xblockexpression = null;
      {
        String _plus = (folder + File.separator);
        String _plus_1 = (_plus + name);
        File _file = new File(_plus_1);
        final File file = _file;
        file.mkdirs();
        file.createNewFile();
        _xblockexpression = (file);
      }
      return _xblockexpression;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static File newFileWithContent(final File file, final CharSequence contents) {
    try {
      File _xblockexpression = null;
      {
        PrintWriter _printWriter = new PrintWriter(file);
        final PrintWriter writer = _printWriter;
        String _string = contents.toString();
        writer.write(_string);
        writer.close();
        _xblockexpression = (file);
      }
      return _xblockexpression;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
