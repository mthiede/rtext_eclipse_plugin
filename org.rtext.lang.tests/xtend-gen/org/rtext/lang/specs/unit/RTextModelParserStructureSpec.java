package org.rtext.lang.specs.unit;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.model.Element;
import org.rtext.lang.specs.unit.RTextModelParserSpec;
import org.rtext.lang.specs.util.IsElement;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("structure")
public class RTextModelParserStructureSpec extends RTextModelParserSpec {
  @Test
  @Named("parse command")
  @Order(0)
  public void _parseCommand() throws Exception {
    List<Element> _parse = this.parse("Type");
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Type");
    boolean _doubleArrow = this.operator_doubleArrow(_first, _element);
    Assert.assertTrue("\nExpected \"Type\".parse.first \t\t\t=> element(\"Type\") but"
     + "\n     \"Type\".parse.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \"Type\".parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
    List<Element> _parse_1 = this.parse("OtherType");
    Element _first_1 = JnarioIterableExtensions.<Element>first(_parse_1);
    IsElement _element_1 = IsElement.element("OtherType");
    boolean _doubleArrow_1 = this.operator_doubleArrow(_first_1, _element_1);
    Assert.assertTrue("\nExpected \"OtherType\".parse.first\t\t=> element(\"OtherType\") but"
     + "\n     \"OtherType\".parse.first is " + new StringDescription().appendValue(_first_1).toString()
     + "\n     \"OtherType\".parse is " + new StringDescription().appendValue(_parse_1).toString()
     + "\n     element(\"OtherType\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow_1);
    
  }
  
  @Test
  @Named("parse name")
  @Order(1)
  public void _parseName() throws Exception {
    List<Element> _parse = this.parse("Type name1");
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Type", "name1");
    boolean _doubleArrow = this.operator_doubleArrow(_first, _element);
    Assert.assertTrue("\nExpected \"Type name1\".parse.first \t\t=> element(\"Type\", \"name1\") but"
     + "\n     \"Type name1\".parse.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \"Type name1\".parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     element(\"Type\", \"name1\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
    List<Element> _parse_1 = this.parse("OtherType name2");
    Element _first_1 = JnarioIterableExtensions.<Element>first(_parse_1);
    IsElement _element_1 = IsElement.element("OtherType", "name2");
    boolean _doubleArrow_1 = this.operator_doubleArrow(_first_1, _element_1);
    Assert.assertTrue("\nExpected \"OtherType name2\".parse.first => element(\"OtherType\", \"name2\") but"
     + "\n     \"OtherType name2\".parse.first is " + new StringDescription().appendValue(_first_1).toString()
     + "\n     \"OtherType name2\".parse is " + new StringDescription().appendValue(_parse_1).toString()
     + "\n     element(\"OtherType\", \"name2\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow_1);
    
  }
  
  @Test
  @Named("parse attributes")
  @Order(2)
  public void _parseAttributes() throws Exception {
    List<Element> _parse = this.parse("Type name1, label: 10");
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Type", "name1");
    boolean _doubleArrow = this.operator_doubleArrow(_first, _element);
    Assert.assertTrue("\nExpected \"Type name1, label: 10\".parse.first => \telement(\"Type\", \"name1\") but"
     + "\n     \"Type name1, label: 10\".parse.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \"Type name1, label: 10\".parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     element(\"Type\", \"name1\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
    List<Element> _parse_1 = this.parse("Type name1, label: \"a string\"");
    Element _first_1 = JnarioIterableExtensions.<Element>first(_parse_1);
    IsElement _element_1 = IsElement.element("Type", "name1");
    boolean _doubleArrow_1 = this.operator_doubleArrow(_first_1, _element_1);
    Assert.assertTrue("\nExpected \'Type name1, label: \"a string\"\'.parse.first => \telement(\"Type\", \"name1\") but"
     + "\n     \'Type name1, label: \"a string\"\'.parse.first is " + new StringDescription().appendValue(_first_1).toString()
     + "\n     \'Type name1, label: \"a string\"\'.parse is " + new StringDescription().appendValue(_parse_1).toString()
     + "\n     element(\"Type\", \"name1\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow_1);
    
    List<Element> _parse_2 = this.parse("Type name1, label: /a/reference");
    Element _first_2 = JnarioIterableExtensions.<Element>first(_parse_2);
    IsElement _element_2 = IsElement.element("Type", "name1");
    boolean _doubleArrow_2 = this.operator_doubleArrow(_first_2, _element_2);
    Assert.assertTrue("\nExpected \'Type name1, label: /a/reference\'.parse.first => element(\"Type\", \"name1\") but"
     + "\n     \'Type name1, label: /a/reference\'.parse.first is " + new StringDescription().appendValue(_first_2).toString()
     + "\n     \'Type name1, label: /a/reference\'.parse is " + new StringDescription().appendValue(_parse_2).toString()
     + "\n     element(\"Type\", \"name1\") is " + new StringDescription().appendValue(_element_2).toString() + "\n", _doubleArrow_2);
    
  }
  
  @Test
  @Ignore
  @Named("TEXT [PENDING]")
  @Order(3)
  public void _tEXT() throws Exception {
  }
  
  @Test
  @Named("ignores comments")
  @Order(4)
  public void _ignoresComments() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# a comment");
    _builder.newLine();
    _builder.append("Type");
    List<Element> _parse = this.parse(_builder);
    int _size = _parse.size();
    boolean _doubleArrow = this.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(1));
    Assert.assertTrue("\nExpected \'\'\'\n\t\t\t# a comment\n\t\t\tType\'\'\'.parse.size => 1 but"
     + "\n     \'\'\'\n\t\t\t# a comment\n\t\t\tType\'\'\'.parse.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     \'\'\'\n\t\t\t# a comment\n\t\t\tType\'\'\'.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     \'\'\'\n\t\t\t# a comment\n\t\t\tType\'\'\' is " + new StringDescription().appendValue(_builder).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("nests list")
  @Order(5)
  public void _nestsList() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type parent{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child child1{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child child2{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    List<Element> _parse = this.parse(_builder);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Child", "child1");
    IsElement _element_1 = IsElement.element("Child", "child2");
    IsElement _element_2 = IsElement.element("Type", "parent", _element, _element_1);
    boolean _doubleArrow = this.operator_doubleArrow(_first, _element_2);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1{\n\t\t\t\t}\n\t\t\t\tChild child2{\n\t\t\t\t}\n\t\t\t}\n\t\t\t\'\'\'.parse.first => \n\t\t\t\telement(\"Type\", \"parent\", \n\t\t\t\t\telement(\"Child\", \"child1\"),\n\t\t\t\t\telement(\"Child\", \"child2\")\n\t\t\t\t) but"
     + "\n     \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1{\n\t\t\t\t}\n\t\t\t\tChild child2{\n\t\t\t\t}\n\t\t\t}\n\t\t\t\'\'\'.parse.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1{\n\t\t\t\t}\n\t\t\t\tChild child2{\n\t\t\t\t}\n\t\t\t}\n\t\t\t\'\'\'.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1{\n\t\t\t\t}\n\t\t\t\tChild child2{\n\t\t\t\t}\n\t\t\t}\n\t\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     element(\"Type\", \"parent\", \n\t\t\t\t\telement(\"Child\", \"child1\"),\n\t\t\t\t\telement(\"Child\", \"child2\")\n\t\t\t\t) is " + new StringDescription().appendValue(_element_2).toString()
     + "\n     element(\"Child\", \"child1\") is " + new StringDescription().appendValue(_element).toString()
     + "\n     element(\"Child\", \"child2\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("nests list without curly braces")
  @Order(6)
  public void _nestsListWithoutCurlyBraces() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type parent{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child child1");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child child2");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    List<Element> _parse = this.parse(_builder);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Child", "child1");
    IsElement _element_1 = IsElement.element("Child", "child2");
    IsElement _element_2 = IsElement.element("Type", "parent", _element, _element_1);
    boolean _doubleArrow = this.operator_doubleArrow(_first, _element_2);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1\n\t\t\t\tChild child2\n\t\t\t}\n\t\t\t\'\'\'.parse.first => \n\t\t\t\telement(\"Type\", \"parent\", \n\t\t\t\t\telement(\"Child\", \"child1\"),\n\t\t\t\t\telement(\"Child\", \"child2\")\n\t\t\t\t) but"
     + "\n     \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1\n\t\t\t\tChild child2\n\t\t\t}\n\t\t\t\'\'\'.parse.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1\n\t\t\t\tChild child2\n\t\t\t}\n\t\t\t\'\'\'.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     \'\'\'\n\t\t\tType parent{\n\t\t\t\tChild child1\n\t\t\t\tChild child2\n\t\t\t}\n\t\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     element(\"Type\", \"parent\", \n\t\t\t\t\telement(\"Child\", \"child1\"),\n\t\t\t\t\telement(\"Child\", \"child2\")\n\t\t\t\t) is " + new StringDescription().appendValue(_element_2).toString()
     + "\n     element(\"Child\", \"child1\") is " + new StringDescription().appendValue(_element).toString()
     + "\n     element(\"Child\", \"child2\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow);
    
  }
}