/**
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 */
package org.rtext.lang.specs.unit.parser;

import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.model.Element;
import org.rtext.lang.specs.unit.parser.RTextModelParserSpec;

@Named("full text region")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class RTextModelParserFullTextRegionSpec extends RTextModelParserSpec {
  @Test
  @Named("spans type and name")
  @Order(1)
  public void _spansTypeAndName() throws Exception {
    String input = "Type parent";
    List<Element> _parse = this.parse(input);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    String region = this.fullText(_first, input);
    Assert.assertTrue("\nExpected region => input but"
     + "\n     region is " + new org.hamcrest.StringDescription().appendValue(region).toString()
     + "\n     input is " + new org.hamcrest.StringDescription().appendValue(input).toString() + "\n", Should.<String>operator_doubleArrow(region, input));
    
  }
  
  @Test
  @Named("spans over children")
  @Order(2)
  public void _spansOverChildren() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    String input = _builder.toString();
    List<Element> _parse = this.parse(input);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    String content = this.fullText(_first, input);
    String _string = input.toString();
    Assert.assertTrue("\nExpected content => input.toString but"
     + "\n     content is " + new org.hamcrest.StringDescription().appendValue(content).toString()
     + "\n     input.toString is " + new org.hamcrest.StringDescription().appendValue(_string).toString()
     + "\n     input is " + new org.hamcrest.StringDescription().appendValue(input).toString() + "\n", Should.<String>operator_doubleArrow(content, _string));
    
  }
  
  @Test
  @Named("spans children with feature def")
  @Order(3)
  public void _spansChildrenWithFeatureDef() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("IntegerType myType { ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("lowerLimit: [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ARLimit   ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("}");
    final String input = _builder.toString();
    List<Element> _parse = this.parse(input);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    String content = this.fullText(_first, input);
    String _string = input.toString();
    Assert.assertTrue("\nExpected content => input.toString but"
     + "\n     content is " + new org.hamcrest.StringDescription().appendValue(content).toString()
     + "\n     input.toString is " + new org.hamcrest.StringDescription().appendValue(_string).toString()
     + "\n     input is " + new org.hamcrest.StringDescription().appendValue(input).toString() + "\n", Should.<String>operator_doubleArrow(content, _string));
    
  }
  
  @Test
  @Named("child element region spans all child childs")
  @Order(4)
  public void _childElementRegionSpansAllChildChilds() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type parent{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child child{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ChildChild childChild");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final String input = _builder.toString();
    List<Element> _parse = this.parse(input);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    List<Element> _children = _first.getChildren();
    Element _first_1 = JnarioIterableExtensions.<Element>first(_children);
    final String content = this.fullText(_first_1, input);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("Child child{");
    _builder_1.newLine();
    _builder_1.append("\t\t");
    _builder_1.append("ChildChild childChild");
    _builder_1.newLine();
    _builder_1.append("\t");
    _builder_1.append("}");
    String _string = _builder_1.toString();
    Assert.assertTrue("\nExpected content => \'\'\'\n\t\t\tChild child{\n\t\t\t\t\tChildChild childChild\n\t\t\t\t}\'\'\'.toString but"
     + "\n     content is " + new org.hamcrest.StringDescription().appendValue(content).toString()
     + "\n     \'\'\'\n\t\t\tChild child{\n\t\t\t\t\tChildChild childChild\n\t\t\t\t}\'\'\'.toString is " + new org.hamcrest.StringDescription().appendValue(_string).toString()
     + "\n     \'\'\'\n\t\t\tChild child{\n\t\t\t\t\tChildChild childChild\n\t\t\t\t}\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder_1).toString() + "\n", Should.<String>operator_doubleArrow(content, _string));
    
  }
  
  @Test
  @Named("deeply nested elements")
  @Order(5)
  public void _deeplyNestedElements() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type parent{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Child child{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ChildChild childChild{");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("ChildChildChild childChildChild\t{");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("X x");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final String input = _builder.toString();
    Element _elements = this.elements(input, "childChildChild");
    final String region = this.fullText(_elements, input);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("ChildChildChild childChildChild\t{");
    _builder_1.newLine();
    _builder_1.append("\t\t\t\t");
    _builder_1.append("X x");
    _builder_1.newLine();
    _builder_1.append("\t\t\t");
    _builder_1.append("}");
    String _string = _builder_1.toString();
    Assert.assertTrue("\nExpected region => \'\'\'\n\t\t\tChildChildChild childChildChild\t{\n\t\t\t\t\t\t\tX x\n\t\t\t\t\t\t}\'\'\'.toString but"
     + "\n     region is " + new org.hamcrest.StringDescription().appendValue(region).toString()
     + "\n     \'\'\'\n\t\t\tChildChildChild childChildChild\t{\n\t\t\t\t\t\t\tX x\n\t\t\t\t\t\t}\'\'\'.toString is " + new org.hamcrest.StringDescription().appendValue(_string).toString()
     + "\n     \'\'\'\n\t\t\tChildChildChild childChildChild\t{\n\t\t\t\t\t\t\tX x\n\t\t\t\t\t\t}\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder_1).toString() + "\n", Should.<String>operator_doubleArrow(region, _string));
    
  }
}
