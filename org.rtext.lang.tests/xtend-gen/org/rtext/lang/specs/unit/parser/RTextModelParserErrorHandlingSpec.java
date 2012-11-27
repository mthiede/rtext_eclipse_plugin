package org.rtext.lang.specs.unit.parser;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.model.Element;
import org.rtext.lang.specs.unit.parser.RTextModelParserSpec;
import org.rtext.lang.specs.util.IsElement;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("error handling")
public class RTextModelParserErrorHandlingSpec extends RTextModelParserSpec {
  @Test
  @Named("ignores too many closing curly braces")
  @Order(14)
  public void _ignoresTooManyClosingCurlyBraces() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type parent{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence input = _builder;
    List<Element> _parse = this.parse(input);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Child");
    IsElement _element_1 = IsElement.element("Type", "parent", _element);
    boolean _doubleArrow = this.operator_doubleArrow(_first, _element_1);
    Assert.assertTrue("\nExpected input.parse.first => element(\"Type\", \"parent\",\n\t\t\t\telement(\"Child\")\n\t\t\t) but"
     + "\n     input.parse.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     input.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     input is " + new StringDescription().appendValue(input).toString()
     + "\n     element(\"Type\", \"parent\",\n\t\t\t\telement(\"Child\")\n\t\t\t) is " + new StringDescription().appendValue(_element_1).toString()
     + "\n     element(\"Child\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("creates no elements for empty string")
  @Order(15)
  public void _createsNoElementsForEmptyString() throws Exception {
    List<Element> _parse = this.parse("");
    int _size = _parse.size();
    boolean _doubleArrow = this.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected \"\".parse.size => 0 but"
     + "\n     \"\".parse.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     \"\".parse is " + new StringDescription().appendValue(_parse).toString() + "\n", _doubleArrow);
    
  }
}
