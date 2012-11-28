package org.rtext.lang.specs.unit.util;

import java.util.List;

import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.ExampleTable;
import org.jnario.lib.ExampleTableIterators;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.util.Strings;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("splitCommand")
public class StringsSplitCommandSpec extends StringsSpec {
  public ExampleTable<StringsSplitCommandSpecExamples> _initStringsSplitCommandSpecExamples() {
    
    List<String> _list = JnarioCollectionLiterals.<String>list("ruby");
    List<String> _list_1 = JnarioCollectionLiterals.<String>list("ruby", "-l");
    List<String> _list_2 = JnarioCollectionLiterals.<String>list("ruby", "-l");
    List<String> _list_3 = JnarioCollectionLiterals.<String>list("ruby", "-l", "arg");return ExampleTable.create("examples", 
      java.util.Arrays.asList("input", "result"), 
      new StringsSplitCommandSpecExamples(  java.util.Arrays.asList("\"ruby\"", "list(\"ruby\")"), "ruby", _list),
      new StringsSplitCommandSpecExamples(  java.util.Arrays.asList("\"ruby -l\"", "list(\"ruby\", \"-l\")"), "ruby -l", _list_1),
      new StringsSplitCommandSpecExamples(  java.util.Arrays.asList("\"ruby  -l\"", "list(\"ruby\", \"-l\")"), "ruby  -l", _list_2),
      new StringsSplitCommandSpecExamples(  java.util.Arrays.asList("\'ruby -l \"arg\"\'", "list(\"ruby\", \"-l\", \"arg\")"), "ruby -l \"arg\"", _list_3)
    );
  }
  
  protected ExampleTable<StringsSplitCommandSpecExamples> examples = _initStringsSplitCommandSpecExamples();
  
  @Test
  @Named("examples.forEach[Strings::splitCommand[input].toList => result]")
  @Order(1)
  public void _examplesForEachStringsSplitCommandInputToListResult() throws Exception {
    final Procedure1<StringsSplitCommandSpecExamples> _function = new Procedure1<StringsSplitCommandSpecExamples>() {
        public void apply(final StringsSplitCommandSpecExamples it) {
          String[] _splitCommand = Strings.splitCommand(it.input);
          List<String> _list = IterableExtensions.<String>toList(((Iterable<? extends String>)Conversions.doWrapArray(_splitCommand)));
          boolean _doubleArrow = Should.operator_doubleArrow(_list, it.result);
          Assert.assertTrue("\nExpected Strings::splitCommand(input).toList => result but"
           + "\n     Strings::splitCommand(input).toList is " + new StringDescription().appendValue(_list).toString()
           + "\n     Strings::splitCommand(input) is " + new StringDescription().appendValue(((Iterable<? extends String>)Conversions.doWrapArray(_splitCommand))).toString()
           + "\n     input is " + new StringDescription().appendValue(it.input).toString()
           + "\n     result is " + new StringDescription().appendValue(it.result).toString() + "\n", _doubleArrow);
          
        }
      };
    ExampleTableIterators.<StringsSplitCommandSpecExamples>forEach(this.examples, _function);
  }
}
