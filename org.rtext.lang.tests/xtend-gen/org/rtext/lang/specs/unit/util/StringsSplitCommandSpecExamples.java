package org.rtext.lang.specs.unit.util;

import java.util.List;
import org.jnario.lib.ExampleTableRow;

@SuppressWarnings("all")
public class StringsSplitCommandSpecExamples extends ExampleTableRow {
  public StringsSplitCommandSpecExamples(final List<String> cellNames, final String input, final List<String> result) {
    super(cellNames);
    this.input = input;
    this.result = result;
    
  }
  
  private String input;
  
  public String getInput() {
    return this.input;
  }
  
  private List<String> result;
  
  public List<String> getResult() {
    return this.result;
  }
}
