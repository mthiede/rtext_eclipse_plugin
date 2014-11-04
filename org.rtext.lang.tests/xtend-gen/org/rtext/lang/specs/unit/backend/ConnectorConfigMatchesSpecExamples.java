package org.rtext.lang.specs.unit.backend;

import java.util.List;
import org.jnario.lib.ExampleTableRow;

@SuppressWarnings("all")
public class ConnectorConfigMatchesSpecExamples extends ExampleTableRow {
  public ConnectorConfigMatchesSpecExamples(final List<String> cellNames, final String fileName, final List<String> patterns, final boolean doesMatch) {
    super(cellNames);
    this.fileName = fileName;
    this.patterns = patterns;
    this.doesMatch = doesMatch;
    
  }
  
  private String fileName;
  
  public String getFileName() {
    return this.fileName;
  }
  
  private List<String> patterns;
  
  public List<String> getPatterns() {
    return this.patterns;
  }
  
  private boolean doesMatch;
  
  public boolean getDoesMatch() {
    return this.doesMatch;
  }
}
