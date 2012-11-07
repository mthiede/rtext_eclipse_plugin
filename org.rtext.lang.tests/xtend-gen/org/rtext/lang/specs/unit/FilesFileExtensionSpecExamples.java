package org.rtext.lang.specs.unit;

import java.util.List;
import org.jnario.lib.ExampleTableRow;

public class FilesFileExtensionSpecExamples extends ExampleTableRow {
  public FilesFileExtensionSpecExamples(final List<String> cellNames, final String filename, final String ext) {
    super(cellNames);
    this.filename = filename;
    this.ext = ext;
  }
  
  public String filename;
  
  public String getFilename() {
    return filename;
  }
  
  public String ext;
  
  public String getExt() {
    return ext;
  }
  
  public List<String> getCells() {
    return java.util.Arrays.asList(toString(filename) ,toString(ext));
  }
}
