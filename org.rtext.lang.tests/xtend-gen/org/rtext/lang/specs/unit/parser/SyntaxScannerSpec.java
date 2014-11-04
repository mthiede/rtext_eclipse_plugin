/**
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 */
package org.rtext.lang.specs.unit.parser;

import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.editor.ColorManager;
import org.rtext.lang.editor.IColorConstants;
import org.rtext.lang.editor.SyntaxScanner;
import org.rtext.lang.model.AbstractRTextParser;
import org.rtext.lang.specs.util.SimpleDocument;

@Named("SyntaxScanner")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class SyntaxScannerSpec {
  public SyntaxScanner subject;
  
  @Before
  public void before() throws Exception {
    ColorManager _colorManager = new ColorManager();
    SyntaxScanner _syntaxScanner = new SyntaxScanner(_colorManager);
    this.subject = _syntaxScanner;
  }
  
  @Test
  @Named("parse comments")
  @Order(1)
  public void _parseComments() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#a comment");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _first = JnarioIterableExtensions.<RGB>first(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t#a comment\n\t\t\'\'\'.scan.first => COMMENT but"
     + "\n     \'\'\'\n\t\t#a comment\n\t\t\'\'\'.scan.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\n\t\t#a comment\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\t#a comment\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     COMMENT is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.COMMENT).toString() + "\n", Should.<RGB>operator_doubleArrow(_first, IColorConstants.COMMENT));
    
  }
  
  @Test
  @Named("parse annotations")
  @Order(2)
  public void _parseAnnotations() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@a comment");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _first = JnarioIterableExtensions.<RGB>first(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t@a comment\n\t\t\'\'\'.scan.first => ANNOTATION but"
     + "\n     \'\'\'\n\t\t@a comment\n\t\t\'\'\'.scan.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\n\t\t@a comment\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\t@a comment\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     ANNOTATION is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.ANNOTATION).toString() + "\n", Should.<RGB>operator_doubleArrow(_first, IColorConstants.ANNOTATION));
    
  }
  
  @Test
  @Named("parse comments until EOL")
  @Order(3)
  public void _parseCommentsUntilEOL() throws Exception {
    List<RGB> _scan = this.scan(("#a comment" + Character.valueOf(AbstractRTextParser.EOL)));
    RGB _first = JnarioIterableExtensions.<RGB>first(_scan);
    Assert.assertTrue("\nExpected (\"#a comment\" + EOL).scan.first => COMMENT but"
     + "\n     (\"#a comment\" + EOL).scan.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     (\"#a comment\" + EOL).scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \"#a comment\" + EOL is " + new org.hamcrest.StringDescription().appendValue(("#a comment" + Character.valueOf(AbstractRTextParser.EOL))).toString()
     + "\n     EOL is " + new org.hamcrest.StringDescription().appendValue(Character.valueOf(AbstractRTextParser.EOL)).toString()
     + "\n     COMMENT is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.COMMENT).toString() + "\n", Should.<RGB>operator_doubleArrow(_first, IColorConstants.COMMENT));
    
  }
  
  @Test
  @Named("parse command")
  @Order(4)
  public void _parseCommand() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: /a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _first = JnarioIterableExtensions.<RGB>first(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.first => COMMAND but"
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     COMMAND is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.COMMAND).toString() + "\n", Should.<RGB>operator_doubleArrow(_first, IColorConstants.COMMAND));
    
  }
  
  @Test
  @Named("parse command starting with \\\'_\\\'")
  @Order(5)
  public void _parseCommandStartingWith() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type _name, label: /a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _first = JnarioIterableExtensions.<RGB>first(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType _name, label: /a/Reference\n\t\t\'\'\'.scan.first => COMMAND but"
     + "\n     \'\'\'\n\t\tType _name, label: /a/Reference\n\t\t\'\'\'.scan.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\n\t\tType _name, label: /a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType _name, label: /a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     COMMAND is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.COMMAND).toString() + "\n", Should.<RGB>operator_doubleArrow(_first, IColorConstants.COMMAND));
    
  }
  
  @Test
  @Named("parse identifier")
  @Order(6)
  public void _parseIdentifier() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: /a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _second = JnarioIterableExtensions.<RGB>second(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.second => IDENTIFIER but"
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.second is " + new org.hamcrest.StringDescription().appendValue(_second).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     IDENTIFIER is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", Should.<RGB>operator_doubleArrow(_second, IColorConstants.IDENTIFIER));
    
  }
  
  @Test
  @Named("parse identifier with \\\'_\\\'")
  @Order(7)
  public void _parseIdentifierWith() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name_with, label: /a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _second = JnarioIterableExtensions.<RGB>second(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name_with, label: /a/Reference\n\t\t\'\'\'.scan.second => IDENTIFIER but"
     + "\n     \'\'\'\n\t\tType name_with, label: /a/Reference\n\t\t\'\'\'.scan.second is " + new org.hamcrest.StringDescription().appendValue(_second).toString()
     + "\n     \'\'\'\n\t\tType name_with, label: /a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name_with, label: /a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     IDENTIFIER is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", Should.<RGB>operator_doubleArrow(_second, IColorConstants.IDENTIFIER));
    
  }
  
  @Test
  @Named("parse separator")
  @Order(8)
  public void _parseSeparator() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: /a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _third = JnarioIterableExtensions.<RGB>third(_scan);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.third => DEFAULT but"
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.third is " + new org.hamcrest.StringDescription().appendValue(_third).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     DEFAULT is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.DEFAULT).toString() + "\n", Should.<RGB>operator_doubleArrow(_third, IColorConstants.DEFAULT));
    
  }
  
  @Test
  @Named("parse label")
  @Order(9)
  public void _parseLabel() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: /a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(5);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.get(5) => LABEL but"
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan.get(5) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: /a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     LABEL is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.LABEL).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.LABEL));
    
  }
  
  @Test
  @Named("parse reference")
  @Order(10)
  public void _parseReference() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: /long/a/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: /long/a/Reference\n\t\t\'\'\'.scan.get(7) => REFERENCE but"
     + "\n     \'\'\'\n\t\tType name, label: /long/a/Reference\n\t\t\'\'\'.scan.get(7) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: /long/a/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: /long/a/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     REFERENCE is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.REFERENCE).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.REFERENCE));
    
  }
  
  @Test
  @Named("parse macros")
  @Order(11)
  public void _parseMacros() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: <generic>");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: <generic>\n\t\t\'\'\'.scan.get(7) => GENERICS but"
     + "\n     \'\'\'\n\t\tType name, label: <generic>\n\t\t\'\'\'.scan.get(7) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: <generic>\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: <generic>\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     GENERICS is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.GENERICS).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.GENERICS));
    
  }
  
  @Test
  @Named("parse reference without leading \\\'/\\\'")
  @Order(12)
  public void _parseReferenceWithoutLeading() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: a/long/Reference");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: a/long/Reference\n\t\t\'\'\'.scan.get(7) => REFERENCE but"
     + "\n     \'\'\'\n\t\tType name, label: a/long/Reference\n\t\t\'\'\'.scan.get(7) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: a/long/Reference\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: a/long/Reference\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     REFERENCE is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.REFERENCE).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.REFERENCE));
    
  }
  
  @Test
  @Named("parse number")
  @Order(13)
  public void _parseNumber() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: 8");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: 8\n\t\t\'\'\'.scan.get(7) => NUMBER but"
     + "\n     \'\'\'\n\t\tType name, label: 8\n\t\t\'\'\'.scan.get(7) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: 8\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: 8\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     NUMBER is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.NUMBER).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.NUMBER));
    
  }
  
  @Test
  @Named("parse string")
  @Order(14)
  public void _parseString() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: \"a string\"");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: \"a string\"\n\t\t\'\'\'.scan.get(7) => STRING but"
     + "\n     \'\'\'\n\t\tType name, label: \"a string\"\n\t\t\'\'\'.scan.get(7) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: \"a string\"\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: \"a string\"\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     STRING is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.STRING).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.STRING));
    
  }
  
  @Test
  @Named("parse enum")
  @Order(15)
  public void _parseEnum() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name, label: enum");
    _builder.newLine();
    List<RGB> _scan = this.scan(_builder);
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tType name, label: enum\n\t\t\'\'\'.scan.get(7) => IDENTIFIER but"
     + "\n     \'\'\'\n\t\tType name, label: enum\n\t\t\'\'\'.scan.get(7) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tType name, label: enum\n\t\t\'\'\'.scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\n\t\tType name, label: enum\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     IDENTIFIER is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.IDENTIFIER));
    
  }
  
  @Test
  @Named("parse whitespace")
  @Order(16)
  public void _parseWhitespace() throws Exception {
    List<RGB> _scan = this.scan("Type name, label: enum\r\n");
    RGB _fifth = JnarioIterableExtensions.<RGB>fifth(_scan);
    Assert.assertTrue("\nExpected \"Type name, label: enum\\r\\n\".scan.fifth => IDENTIFIER but"
     + "\n     \"Type name, label: enum\\r\\n\".scan.fifth is " + new org.hamcrest.StringDescription().appendValue(_fifth).toString()
     + "\n     \"Type name, label: enum\\r\\n\".scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     IDENTIFIER is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", Should.<RGB>operator_doubleArrow(_fifth, IColorConstants.IDENTIFIER));
    
  }
  
  @Test
  @Named("parse whitespace after command")
  @Order(17)
  public void _parseWhitespaceAfterCommand() throws Exception {
    final String input = "Type   ";
    this.scan(input);
    int _tokenOffset = this.subject.getTokenOffset();
    int _tokenLength = this.subject.getTokenLength();
    int _plus = (_tokenOffset + _tokenLength);
    int _length = input.length();
    boolean _lessEqualsThan = (_plus <= _length);
    Assert.assertTrue("\nExpected subject.tokenOffset + subject.tokenLength <= input.length but"
     + "\n     subject.tokenOffset + subject.tokenLength is " + new org.hamcrest.StringDescription().appendValue(_plus).toString()
     + "\n     subject.tokenOffset is " + new org.hamcrest.StringDescription().appendValue(_tokenOffset).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
     + "\n     subject.tokenLength is " + new org.hamcrest.StringDescription().appendValue(_tokenLength).toString()
     + "\n     input.length is " + new org.hamcrest.StringDescription().appendValue(_length).toString()
     + "\n     input is " + new org.hamcrest.StringDescription().appendValue(input).toString() + "\n", _lessEqualsThan);
    
  }
  
  @Test
  @Named("parse string until EOL")
  @Order(18)
  public void _parseStringUntilEOL() throws Exception {
    List<RGB> _scan = this.scan(("Type name, label: \"a string " + Character.valueOf(AbstractRTextParser.EOL)));
    RGB _get = _scan.get(7);
    Assert.assertTrue("\nExpected (\'Type name, label: \"a string \' + EOL).scan.get(7) => STRING but"
     + "\n     \'Type name, label: \"a string \' + EOL).scan.get(7 is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     (\'Type name, label: \"a string \' + EOL).scan is " + new org.hamcrest.StringDescription().appendValue(_scan).toString()
     + "\n     \'Type name, label: \"a string \' + EOL is " + new org.hamcrest.StringDescription().appendValue(("Type name, label: \"a string " + Character.valueOf(AbstractRTextParser.EOL))).toString()
     + "\n     EOL is " + new org.hamcrest.StringDescription().appendValue(Character.valueOf(AbstractRTextParser.EOL)).toString()
     + "\n     STRING is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.STRING).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.STRING));
    
  }
  
  @Test
  @Named("positions macros correctly")
  @Order(19)
  public void _positionsMacrosCorrectly() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("EPackage{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("EClass{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("EOperation{");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("EAnnotation source:<source> ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    _builder.newLine();
    List<String> _regions = this.regions(_builder);
    String _get = _regions.get(12);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<source> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\'.regions.get(12) => \"<source>\" but"
     + "\n     \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<source> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\'.regions.get(12) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<source> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\'.regions is " + new org.hamcrest.StringDescription().appendValue(_regions).toString()
     + "\n     \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<source> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString() + "\n", Should.<String>operator_doubleArrow(_get, "<source>"));
    
  }
  
  @Test
  @Named("support escaped macros")
  @Order(20)
  public void _supportEscapedMacros() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("EPackage{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("EClass{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("EOperation{");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("EAnnotation source:<%text>text%> ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    _builder.newLine();
    List<String> _regions = this.regions(_builder);
    String _get = _regions.get(12);
    Assert.assertTrue("\nExpected \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<%text>text%> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\'.regions.get(12) => \"<%text>text%>\" but"
     + "\n     \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<%text>text%> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\'.regions.get(12) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<%text>text%> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\'.regions is " + new org.hamcrest.StringDescription().appendValue(_regions).toString()
     + "\n     \'\'\'\n\t\tEPackage{\n\t\t\tEClass{\n\t\t\t\tEOperation{\n\t\t\t\t\tEAnnotation source:<%text>text%> \n\t\t\t\t}\n\t\t\t}\n\t\t}\t\n\n\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString() + "\n", Should.<String>operator_doubleArrow(_get, "<%text>text%>"));
    
  }
  
  @Test
  @Named("support escaped macros without end")
  @Order(21)
  public void _supportEscapedMacrosWithoutEnd() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("EPackage{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("EClass{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("EOperation{");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("EAnnotation source:<%text>text");
    List<String> _regions = this.regions(_builder);
    final String region = _regions.get(12);
    Assert.assertTrue("\nExpected region => \"<%text>text\" but"
     + "\n     region is " + new org.hamcrest.StringDescription().appendValue(region).toString() + "\n", Should.<String>operator_doubleArrow(region, "<%text>text"));
    
  }
  
  public List<String> regions(final CharSequence s) {
    List<String> _xblockexpression = null;
    {
      String _string = s.toString();
      final SimpleDocument document = new SimpleDocument(_string);
      int _length = s.length();
      this.subject.setRange(document, 0, _length);
      final List<String> regions = JnarioCollectionLiterals.<String>list();
      IToken token = this.subject.nextToken();
      while ((!token.isEOF())) {
        {
          int _tokenOffset = this.subject.getTokenOffset();
          int _tokenLength = this.subject.getTokenLength();
          String _get = document.get(_tokenOffset, _tokenLength);
          regions.add(_get);
          IToken _nextToken = this.subject.nextToken();
          token = _nextToken;
        }
      }
      _xblockexpression = regions;
    }
    return _xblockexpression;
  }
  
  public List<RGB> scan(final CharSequence s) {
    List<RGB> _xblockexpression = null;
    {
      String _string = s.toString();
      final SimpleDocument document = new SimpleDocument(_string);
      int _length = s.length();
      this.subject.setRange(document, 0, _length);
      final List<IToken> tokens = JnarioCollectionLiterals.<IToken>list();
      IToken token = this.subject.nextToken();
      while ((!token.isEOF())) {
        {
          tokens.add(token);
          IToken _nextToken = this.subject.nextToken();
          token = _nextToken;
        }
      }
      final Function1<IToken, RGB> _function = new Function1<IToken, RGB>() {
        public RGB apply(final IToken it) {
          RGB _xblockexpression = null;
          {
            Object _data = it.getData();
            final TextAttribute attr = ((TextAttribute) _data);
            Color _foreground = attr.getForeground();
            _xblockexpression = _foreground.getRGB();
          }
          return _xblockexpression;
        }
      };
      _xblockexpression = ListExtensions.<IToken, RGB>map(tokens, _function);
    }
    return _xblockexpression;
  }
}
