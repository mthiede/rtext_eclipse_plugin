package org.rtext.lang.specs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class Files {
  public static String read(final String path) {
    try {
      File _file = new File(path);
      final FileInputStream stream = new FileInputStream(_file);
      try {
        final FileChannel fc = stream.getChannel();
        long _size = fc.size();
        final MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, _size);
        Charset _defaultCharset = Charset.defaultCharset();
        CharBuffer _decode = _defaultCharset.decode(bb);
        return _decode.toString();
      } finally {
        stream.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static File newFile(final File folder, final String name) {
    try {
      File _xblockexpression = null;
      {
        String _plus = (folder + File.separator);
        String _plus_1 = (_plus + name);
        final File file = new File(_plus_1);
        file.mkdirs();
        file.createNewFile();
        _xblockexpression = file;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static File newFileWithContent(final File file, final CharSequence contents) {
    try {
      File _xblockexpression = null;
      {
        final PrintWriter writer = new PrintWriter(file);
        String _string = contents.toString();
        writer.write(_string);
        writer.close();
        _xblockexpression = file;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
