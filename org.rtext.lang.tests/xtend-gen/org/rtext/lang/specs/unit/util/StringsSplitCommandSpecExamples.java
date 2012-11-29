package org.rtext.lang.specs.unit.util;

import java.util.List;
import org.jnario.lib.ExampleTableRow;

public class StringsSplitCommandSpecExamples extends ExampleTableRow {
  public StringsSplitCommandSpecExamples(final List<String> cellNames, final String input, final List<String> result) {
    super(cellNames);
    this.input = input;
    this.result = result;
  }
  
  public String input;
  
  public String getInput() {
    return input;
  }
  
  public List<String> result;
  
  public List<String> getResult() {
    return result;
  }
  
  public List<String> getCells() {
    return java.util.Arrays.asList(toString(input) ,toString(result));
  }
}
