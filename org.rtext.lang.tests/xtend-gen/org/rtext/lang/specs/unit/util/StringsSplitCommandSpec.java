package org.rtext.lang.specs.unit.util;

import java.util.Arrays;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.lib.Assert;
import org.jnario.lib.Each;
import org.jnario.lib.ExampleTable;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.unit.util.StringsSpec;
import org.rtext.lang.specs.unit.util.StringsSplitCommandSpecExamples;
import org.rtext.lang.util.Strings;

@Named("splitCommand")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class StringsSplitCommandSpec extends StringsSpec {
  public ExampleTable<StringsSplitCommandSpecExamples> _initStringsSplitCommandSpecExamples() {
    return ExampleTable.create("examples", 
      Arrays.asList("input", "result"), 
      new StringsSplitCommandSpecExamples(  Arrays.asList("\"ruby\"", "list(\"ruby\")"), _initStringsSplitCommandSpecExamplesCell0(), _initStringsSplitCommandSpecExamplesCell1()),
      new StringsSplitCommandSpecExamples(  Arrays.asList("\"ruby -l\"", "list(\"ruby\", \"-l\")"), _initStringsSplitCommandSpecExamplesCell2(), _initStringsSplitCommandSpecExamplesCell3()),
      new StringsSplitCommandSpecExamples(  Arrays.asList("\"ruby  -l\"", "list(\"ruby\", \"-l\")"), _initStringsSplitCommandSpecExamplesCell4(), _initStringsSplitCommandSpecExamplesCell5()),
      new StringsSplitCommandSpecExamples(  Arrays.asList("\'ruby -l \"arg\"\'", "list(\"ruby\", \"-l\", \"arg\")"), _initStringsSplitCommandSpecExamplesCell6(), _initStringsSplitCommandSpecExamplesCell7())
    );
  }
  
  protected ExampleTable<StringsSplitCommandSpecExamples> examples = _initStringsSplitCommandSpecExamples();
  
  public String _initStringsSplitCommandSpecExamplesCell0() {
    return "ruby";
  }
  
  public List<String> _initStringsSplitCommandSpecExamplesCell1() {
    List<String> _list = JnarioCollectionLiterals.<String>list("ruby");
    return _list;
  }
  
  public String _initStringsSplitCommandSpecExamplesCell2() {
    return "ruby -l";
  }
  
  public List<String> _initStringsSplitCommandSpecExamplesCell3() {
    List<String> _list = JnarioCollectionLiterals.<String>list("ruby", "-l");
    return _list;
  }
  
  public String _initStringsSplitCommandSpecExamplesCell4() {
    return "ruby  -l";
  }
  
  public List<String> _initStringsSplitCommandSpecExamplesCell5() {
    List<String> _list = JnarioCollectionLiterals.<String>list("ruby", "-l");
    return _list;
  }
  
  public String _initStringsSplitCommandSpecExamplesCell6() {
    return "ruby -l \"arg\"";
  }
  
  public List<String> _initStringsSplitCommandSpecExamplesCell7() {
    List<String> _list = JnarioCollectionLiterals.<String>list("ruby", "-l", "arg");
    return _list;
  }
  
  @Test
  @Named("examples.forEach[Strings::splitCommand[input].toList => result]")
  @Order(1)
  public void _examplesForEachStringsSplitCommandInputToListResult() throws Exception {
    final Procedure1<StringsSplitCommandSpecExamples> _function = new Procedure1<StringsSplitCommandSpecExamples>() {
      public void apply(final StringsSplitCommandSpecExamples it) {
        String _input = it.getInput();
        String[] _splitCommand = Strings.splitCommand(_input);
        List<String> _list = IterableExtensions.<String>toList(((Iterable<String>)Conversions.doWrapArray(_splitCommand)));
        List<String> _result = it.getResult();
        Assert.assertTrue("\nExpected Strings::splitCommand(input).toList => result but"
         + "\n     Strings::splitCommand(input).toList is " + new org.hamcrest.StringDescription().appendValue(_list).toString()
         + "\n     Strings::splitCommand(input) is " + new org.hamcrest.StringDescription().appendValue(((Iterable<String>)Conversions.doWrapArray(_splitCommand))).toString()
         + "\n     Strings is " + new org.hamcrest.StringDescription().appendValue(Strings.class).toString()
         + "\n     input is " + new org.hamcrest.StringDescription().appendValue(_input).toString()
         + "\n     result is " + new org.hamcrest.StringDescription().appendValue(_result).toString() + "\n", Should.<List<String>>operator_doubleArrow(_list, _result));
        
      }
    };
    Each.<StringsSplitCommandSpecExamples>forEach(this.examples, _function);
  }
}
