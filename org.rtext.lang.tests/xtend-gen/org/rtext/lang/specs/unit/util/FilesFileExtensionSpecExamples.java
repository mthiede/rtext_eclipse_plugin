package org.rtext.lang.specs.unit.util;

import java.util.List;
import org.jnario.lib.ExampleTableRow;

@SuppressWarnings("all")
public class FilesFileExtensionSpecExamples extends ExampleTableRow {
  public FilesFileExtensionSpecExamples(final List<String> cellNames, final String filename, final String ext) {
    super(cellNames);
    this.filename = filename;
    this.ext = ext;
    
  }
  
  private String filename;
  
  public String getFilename() {
    return this.filename;
  }
  
  private String ext;
  
  public String getExt() {
    return this.ext;
  }
}
