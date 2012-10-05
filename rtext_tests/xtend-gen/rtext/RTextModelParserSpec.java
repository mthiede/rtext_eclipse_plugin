package rtext;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.runner.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.model.Element;
import org.rtext.model.ElementBuilder;
import org.rtext.model.RTextModelParser;
import rtext.SimpleDocument;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("RTextModelParser")
public class RTextModelParserSpec {
  @Subject
  public RTextModelParser subject;
  
  @Test
  @Named("parse command")
  @Order(99)
  public void _parseCommand() throws Exception {
    List<Element> _parse = this.parse("Type");
    ElementBuilder _element = ElementBuilder.element("Type");
    ElementBuilder _offset = _element.offset(0);
    ElementBuilder _length = _offset.length(4);
    List<Element> _elements = this.elements(_length);
    boolean _doubleArrow = Should.operator_doubleArrow(_parse, _elements);
    Assert.assertTrue("\nExpected \"Type\".parse \t\t\t=> elements(element(\"Type\").offset(0).length(4)) but"
     + "\n     \"Type\".parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     elements(element(\"Type\").offset(0).length(4)) is " + new StringDescription().appendValue(_elements).toString()
     + "\n     element(\"Type\").offset(0).length(4) is " + new StringDescription().appendValue(_length).toString()
     + "\n     element(\"Type\").offset(0) is " + new StringDescription().appendValue(_offset).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
    List<Element> _parse_1 = this.parse("OtherType");
    ElementBuilder _element_1 = ElementBuilder.element("OtherType");
    ElementBuilder _offset_1 = _element_1.offset(0);
    ElementBuilder _length_1 = _offset_1.length(9);
    List<Element> _elements_1 = this.elements(_length_1);
    boolean _doubleArrow_1 = Should.operator_doubleArrow(_parse_1, _elements_1);
    Assert.assertTrue("\nExpected \"OtherType\".parse\t\t=> elements(element(\"OtherType\").offset(0).length(9)) but"
     + "\n     \"OtherType\".parse is " + new StringDescription().appendValue(_parse_1).toString()
     + "\n     elements(element(\"OtherType\").offset(0).length(9)) is " + new StringDescription().appendValue(_elements_1).toString()
     + "\n     element(\"OtherType\").offset(0).length(9) is " + new StringDescription().appendValue(_length_1).toString()
     + "\n     element(\"OtherType\").offset(0) is " + new StringDescription().appendValue(_offset_1).toString()
     + "\n     element(\"OtherType\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow_1);
    
  }
  
  @Test
  @Named("parse name")
  @Order(99)
  public void _parseName() throws Exception {
    List<Element> _parse = this.parse("Type name1");
    ElementBuilder _element = ElementBuilder.element("Type");
    ElementBuilder _name = _element.name("name1");
    ElementBuilder _offset = _name.offset(0);
    ElementBuilder _length = _offset.length(10);
    List<Element> _elements = this.elements(_length);
    boolean _doubleArrow = Should.operator_doubleArrow(_parse, _elements);
    Assert.assertTrue("\nExpected \"Type name1\".parse \t\t=> elements(element(\"Type\").name(\"name1\").offset(0).length(10)) but"
     + "\n     \"Type name1\".parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     elements(element(\"Type\").name(\"name1\").offset(0).length(10)) is " + new StringDescription().appendValue(_elements).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0).length(10) is " + new StringDescription().appendValue(_length).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0) is " + new StringDescription().appendValue(_offset).toString()
     + "\n     element(\"Type\").name(\"name1\") is " + new StringDescription().appendValue(_name).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
    List<Element> _parse_1 = this.parse("OtherType name2");
    ElementBuilder _element_1 = ElementBuilder.element("OtherType");
    ElementBuilder _name_1 = _element_1.name("name2");
    ElementBuilder _offset_1 = _name_1.offset(0);
    ElementBuilder _length_1 = _offset_1.length(15);
    List<Element> _elements_1 = this.elements(_length_1);
    boolean _doubleArrow_1 = Should.operator_doubleArrow(_parse_1, _elements_1);
    Assert.assertTrue("\nExpected \"OtherType name2\".parse => elements(element(\"OtherType\").name(\"name2\").offset(0).length(15)) but"
     + "\n     \"OtherType name2\".parse is " + new StringDescription().appendValue(_parse_1).toString()
     + "\n     elements(element(\"OtherType\").name(\"name2\").offset(0).length(15)) is " + new StringDescription().appendValue(_elements_1).toString()
     + "\n     element(\"OtherType\").name(\"name2\").offset(0).length(15) is " + new StringDescription().appendValue(_length_1).toString()
     + "\n     element(\"OtherType\").name(\"name2\").offset(0) is " + new StringDescription().appendValue(_offset_1).toString()
     + "\n     element(\"OtherType\").name(\"name2\") is " + new StringDescription().appendValue(_name_1).toString()
     + "\n     element(\"OtherType\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow_1);
    
  }
  
  @Test
  @Named("parse attributes")
  @Order(99)
  public void _parseAttributes() throws Exception {
    List<Element> _parse = this.parse("Type name1, label: 10");
    ElementBuilder _element = ElementBuilder.element("Type");
    ElementBuilder _name = _element.name("name1");
    ElementBuilder _offset = _name.offset(0);
    ElementBuilder _length = _offset.length(21);
    List<Element> _elements = this.elements(_length);
    boolean _doubleArrow = Should.operator_doubleArrow(_parse, _elements);
    Assert.assertTrue("\nExpected \"Type name1, label: 10\".parse => elements(\n\t\t\telement(\"Type\").name(\"name1\").offset(0).length(21)\n\t\t) but"
     + "\n     \"Type name1, label: 10\".parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     elements(\n\t\t\telement(\"Type\").name(\"name1\").offset(0).length(21)\n\t\t) is " + new StringDescription().appendValue(_elements).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0).length(21) is " + new StringDescription().appendValue(_length).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0) is " + new StringDescription().appendValue(_offset).toString()
     + "\n     element(\"Type\").name(\"name1\") is " + new StringDescription().appendValue(_name).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
    List<Element> _parse_1 = this.parse("Type name1, label: \"a string\"");
    ElementBuilder _element_1 = ElementBuilder.element("Type");
    ElementBuilder _name_1 = _element_1.name("name1");
    ElementBuilder _offset_1 = _name_1.offset(0);
    ElementBuilder _length_1 = _offset_1.length(29);
    List<Element> _elements_1 = this.elements(_length_1);
    boolean _doubleArrow_1 = Should.operator_doubleArrow(_parse_1, _elements_1);
    Assert.assertTrue("\nExpected \'Type name1, label: \"a string\"\'.parse => elements(\n\t\t\telement(\"Type\").name(\"name1\").offset(0).length(29)\n\t\t) but"
     + "\n     \'Type name1, label: \"a string\"\'.parse is " + new StringDescription().appendValue(_parse_1).toString()
     + "\n     elements(\n\t\t\telement(\"Type\").name(\"name1\").offset(0).length(29)\n\t\t) is " + new StringDescription().appendValue(_elements_1).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0).length(29) is " + new StringDescription().appendValue(_length_1).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0) is " + new StringDescription().appendValue(_offset_1).toString()
     + "\n     element(\"Type\").name(\"name1\") is " + new StringDescription().appendValue(_name_1).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element_1).toString() + "\n", _doubleArrow_1);
    
    List<Element> _parse_2 = this.parse("Type name1, label: /a/reference");
    ElementBuilder _element_2 = ElementBuilder.element("Type");
    ElementBuilder _name_2 = _element_2.name("name1");
    ElementBuilder _offset_2 = _name_2.offset(0);
    ElementBuilder _length_2 = _offset_2.length(31);
    List<Element> _elements_2 = this.elements(_length_2);
    boolean _doubleArrow_2 = Should.operator_doubleArrow(_parse_2, _elements_2);
    Assert.assertTrue("\nExpected \'Type name1, label: /a/reference\'.parse => elements(\n\t\t\telement(\"Type\").name(\"name1\").offset(0).length(31)\n\t\t) but"
     + "\n     \'Type name1, label: /a/reference\'.parse is " + new StringDescription().appendValue(_parse_2).toString()
     + "\n     elements(\n\t\t\telement(\"Type\").name(\"name1\").offset(0).length(31)\n\t\t) is " + new StringDescription().appendValue(_elements_2).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0).length(31) is " + new StringDescription().appendValue(_length_2).toString()
     + "\n     element(\"Type\").name(\"name1\").offset(0) is " + new StringDescription().appendValue(_offset_2).toString()
     + "\n     element(\"Type\").name(\"name1\") is " + new StringDescription().appendValue(_name_2).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element_2).toString() + "\n", _doubleArrow_2);
    
  }
  
  @Test
  @Named("ignores comments")
  @Order(99)
  public void _ignoresComments() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("# a comment");
    _builder.newLine();
    _builder.append("Type");
    List<Element> _parse = this.parse(_builder);
    ElementBuilder _element = ElementBuilder.element("Type");
    ElementBuilder _offset = _element.offset(11);
    ElementBuilder _length = _offset.length(5);
    List<Element> _elements = this.elements(_length);
    boolean _doubleArrow = Should.operator_doubleArrow(_parse, _elements);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t# a comment\n\t\tType\'\'\'.parse\t=> elements(element(\"Type\").offset(11).length(5)) but"
     + "\n     \'\'\'\n\t\t# a comment\n\t\tType\'\'\'.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     \'\'\'\n\t\t# a comment\n\t\tType\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     elements(element(\"Type\").offset(11).length(5)) is " + new StringDescription().appendValue(_elements).toString()
     + "\n     element(\"Type\").offset(11).length(5) is " + new StringDescription().appendValue(_length).toString()
     + "\n     element(\"Type\").offset(11) is " + new StringDescription().appendValue(_offset).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("nests elements")
  @Order(99)
  public void _nestsElements() throws Exception {
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
    ElementBuilder _element = ElementBuilder.element("Type");
    ElementBuilder _name = _element.name("parent");
    ElementBuilder _offset = _name.offset(0);
    ElementBuilder _length = _offset.length(12);
    ElementBuilder _element_1 = ElementBuilder.element("Child");
    ElementBuilder _name_1 = _element_1.name("child1");
    ElementBuilder _offset_1 = _name_1.offset(12);
    ElementBuilder _length_1 = _offset_1.length(18);
    ElementBuilder _element_2 = ElementBuilder.element("Child");
    ElementBuilder _name_2 = _element_2.name("child2");
    ElementBuilder _offset_2 = _name_2.offset(30);
    ElementBuilder _length_2 = _offset_2.length(20);
    ElementBuilder _children = _length.children(_length_1, _length_2);
    List<Element> _elements = this.elements(_children);
    boolean _doubleArrow = Should.operator_doubleArrow(_parse, _elements);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType parent{\n\t\t\tChild child1{\n\t\t\t}\n\t\t\tChild child2{\n\t\t\t}\n\t\t}\n\t\t\'\'\'.parse => elements(\n\t\t\telement(\"Type\").name(\"parent\").offset(0).length(12).children(\n\t\t\t\telement(\"Child\").name(\"child1\").offset(12).length(18),\n\t\t\t\telement(\"Child\").name(\"child2\").offset(30).length(20)\n\t\t\t)\n\t\t) but"
     + "\n     \'\'\'\n\t\tType parent{\n\t\t\tChild child1{\n\t\t\t}\n\t\t\tChild child2{\n\t\t\t}\n\t\t}\n\t\t\'\'\'.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     \'\'\'\n\t\tType parent{\n\t\t\tChild child1{\n\t\t\t}\n\t\t\tChild child2{\n\t\t\t}\n\t\t}\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     elements(\n\t\t\telement(\"Type\").name(\"parent\").offset(0).length(12).children(\n\t\t\t\telement(\"Child\").name(\"child1\").offset(12).length(18),\n\t\t\t\telement(\"Child\").name(\"child2\").offset(30).length(20)\n\t\t\t)\n\t\t) is " + new StringDescription().appendValue(_elements).toString()
     + "\n     element(\"Type\").name(\"parent\").offset(0).length(12).children(\n\t\t\t\telement(\"Child\").name(\"child1\").offset(12).length(18),\n\t\t\t\telement(\"Child\").name(\"child2\").offset(30).length(20)\n\t\t\t) is " + new StringDescription().appendValue(_children).toString()
     + "\n     element(\"Type\").name(\"parent\").offset(0).length(12) is " + new StringDescription().appendValue(_length).toString()
     + "\n     element(\"Type\").name(\"parent\").offset(0) is " + new StringDescription().appendValue(_offset).toString()
     + "\n     element(\"Type\").name(\"parent\") is " + new StringDescription().appendValue(_name).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString()
     + "\n     element(\"Child\").name(\"child1\").offset(12).length(18) is " + new StringDescription().appendValue(_length_1).toString()
     + "\n     element(\"Child\").name(\"child1\").offset(12) is " + new StringDescription().appendValue(_offset_1).toString()
     + "\n     element(\"Child\").name(\"child1\") is " + new StringDescription().appendValue(_name_1).toString()
     + "\n     element(\"Child\") is " + new StringDescription().appendValue(_element_1).toString()
     + "\n     element(\"Child\").name(\"child2\").offset(30).length(20) is " + new StringDescription().appendValue(_length_2).toString()
     + "\n     element(\"Child\").name(\"child2\").offset(30) is " + new StringDescription().appendValue(_offset_2).toString()
     + "\n     element(\"Child\").name(\"child2\") is " + new StringDescription().appendValue(_name_2).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("nests elements without curly braces")
  @Order(99)
  public void _nestsElementsWithoutCurlyBraces() throws Exception {
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
    ElementBuilder _element = ElementBuilder.element("Type");
    ElementBuilder _name = _element.name("parent");
    ElementBuilder _offset = _name.offset(0);
    ElementBuilder _length = _offset.length(12);
    ElementBuilder _element_1 = ElementBuilder.element("Child");
    ElementBuilder _name_1 = _element_1.name("child1");
    ElementBuilder _offset_1 = _name_1.offset(12);
    ElementBuilder _length_1 = _offset_1.length(14);
    ElementBuilder _element_2 = ElementBuilder.element("Child");
    ElementBuilder _name_2 = _element_2.name("child2");
    ElementBuilder _offset_2 = _name_2.offset(26);
    ElementBuilder _length_2 = _offset_2.length(16);
    ElementBuilder _children = _length.children(_length_1, _length_2);
    List<Element> _elements = this.elements(_children);
    boolean _doubleArrow = Should.operator_doubleArrow(_parse, _elements);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType parent{\n\t\t\tChild child1\n\t\t\tChild child2\n\t\t}\n\t\t\'\'\'.parse => elements(\n\t\t\telement(\"Type\").name(\"parent\").offset(0).length(12).children(\n\t\t\t\telement(\"Child\").name(\"child1\").offset(12).length(14),\n\t\t\t\telement(\"Child\").name(\"child2\").offset(26).length(16)\n\t\t\t)\n\t\t) but"
     + "\n     \'\'\'\n\t\tType parent{\n\t\t\tChild child1\n\t\t\tChild child2\n\t\t}\n\t\t\'\'\'.parse is " + new StringDescription().appendValue(_parse).toString()
     + "\n     \'\'\'\n\t\tType parent{\n\t\t\tChild child1\n\t\t\tChild child2\n\t\t}\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     elements(\n\t\t\telement(\"Type\").name(\"parent\").offset(0).length(12).children(\n\t\t\t\telement(\"Child\").name(\"child1\").offset(12).length(14),\n\t\t\t\telement(\"Child\").name(\"child2\").offset(26).length(16)\n\t\t\t)\n\t\t) is " + new StringDescription().appendValue(_elements).toString()
     + "\n     element(\"Type\").name(\"parent\").offset(0).length(12).children(\n\t\t\t\telement(\"Child\").name(\"child1\").offset(12).length(14),\n\t\t\t\telement(\"Child\").name(\"child2\").offset(26).length(16)\n\t\t\t) is " + new StringDescription().appendValue(_children).toString()
     + "\n     element(\"Type\").name(\"parent\").offset(0).length(12) is " + new StringDescription().appendValue(_length).toString()
     + "\n     element(\"Type\").name(\"parent\").offset(0) is " + new StringDescription().appendValue(_offset).toString()
     + "\n     element(\"Type\").name(\"parent\") is " + new StringDescription().appendValue(_name).toString()
     + "\n     element(\"Type\") is " + new StringDescription().appendValue(_element).toString()
     + "\n     element(\"Child\").name(\"child1\").offset(12).length(14) is " + new StringDescription().appendValue(_length_1).toString()
     + "\n     element(\"Child\").name(\"child1\").offset(12) is " + new StringDescription().appendValue(_offset_1).toString()
     + "\n     element(\"Child\").name(\"child1\") is " + new StringDescription().appendValue(_name_1).toString()
     + "\n     element(\"Child\") is " + new StringDescription().appendValue(_element_1).toString()
     + "\n     element(\"Child\").name(\"child2\").offset(26).length(16) is " + new StringDescription().appendValue(_length_2).toString()
     + "\n     element(\"Child\").name(\"child2\").offset(26) is " + new StringDescription().appendValue(_offset_2).toString()
     + "\n     element(\"Child\").name(\"child2\") is " + new StringDescription().appendValue(_name_2).toString() + "\n", _doubleArrow);
    
  }
  
  public List<Element> elements(final ElementBuilder... elements) {
    final Function1<ElementBuilder,Element> _function = new Function1<ElementBuilder,Element>() {
        public Element apply(final ElementBuilder it) {
          Element _build = it.build();
          return _build;
        }
      };
    List<Element> _map = ListExtensions.<ElementBuilder, Element>map(((List<ElementBuilder>)Conversions.doWrapArray(elements)), _function);
    return _map;
  }
  
  public List<Element> parse(final CharSequence s) {
    List<Element> _xblockexpression = null;
    {
      String _string = s.toString();
      SimpleDocument _simpleDocument = new SimpleDocument(_string);
      final SimpleDocument document = _simpleDocument;
      int _length = s.length();
      this.subject.setRange(document, 0, _length);
      List<Element> _parse = this.subject.parse();
      _xblockexpression = (_parse);
    }
    return _xblockexpression;
  }
}
