package org.rtext.lang.specs.unit.backend;

import java.util.List;

import org.jnario.lib.ExampleTableRow;

public class ConnectorConfigMatchesSpecExamples extends ExampleTableRow {
  public ConnectorConfigMatchesSpecExamples(final List<String> cellNames, final String fileName, final List<String> patterns, final boolean doesMatch) {
    super(cellNames);
    this.fileName = fileName;
    this.patterns = patterns;
    this.doesMatch = doesMatch;
  }
  
  public String fileName;
  
  public String getFileName() {
    return fileName;
  }
  
  public List<String> patterns;
  
  public List<String> getPatterns() {
    return patterns;
  }
  
  public boolean doesMatch;
  
  public boolean getDoesMatch() {
    return doesMatch;
  }
  
  public List<String> getCells() {
    return java.util.Arrays.asList(toString(fileName) ,toString(patterns) ,toString(doesMatch));
  }
}
