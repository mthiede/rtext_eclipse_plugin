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
import org.rtext.lang.specs.util.IsElement;

@Named("error handling")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class RTextModelParserErrorHandlingSpec extends RTextModelParserSpec {
  @Test
  @Named("ignores too many closing curly braces")
  @Order(1)
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
    final String input = _builder.toString();
    List<Element> _parse = this.parse(input);
    Element _first = JnarioIterableExtensions.<Element>first(_parse);
    IsElement _element = IsElement.element("Child");
    IsElement _element_1 = IsElement.element("Type", "parent", _element);
    Assert.assertTrue("\nExpected input.parse.first => element(\"Type\", \"parent\",\n\t\t\t\telement(\"Child\")\n\t\t\t) but"
     + "\n     input.parse.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     input.parse is " + new org.hamcrest.StringDescription().appendValue(_parse).toString()
     + "\n     input is " + new org.hamcrest.StringDescription().appendValue(input).toString()
     + "\n     element(\"Type\", \"parent\",\n\t\t\t\telement(\"Child\")\n\t\t\t) is " + new org.hamcrest.StringDescription().appendValue(_element_1).toString()
     + "\n     element(\"Child\") is " + new org.hamcrest.StringDescription().appendValue(_element).toString() + "\n", Should.<Element>operator_doubleArrow(_first, _element_1));
    
  }
  
  @Test
  @Named("creates no elements for empty string")
  @Order(2)
  public void _createsNoElementsForEmptyString() throws Exception {
    List<Element> _parse = this.parse("");
    int _size = _parse.size();
    Assert.assertTrue("\nExpected \"\".parse.size => 0 but"
     + "\n     \"\".parse.size is " + new org.hamcrest.StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     \"\".parse is " + new org.hamcrest.StringDescription().appendValue(_parse).toString() + "\n", Should.<Integer>operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0)));
    
  }
}
