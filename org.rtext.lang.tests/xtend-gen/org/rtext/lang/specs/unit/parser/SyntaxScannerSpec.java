package org.rtext.lang.specs.unit.parser;

import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.hamcrest.StringDescription;
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

@SuppressWarnings("all")
@Named("SyntaxScanner")
@RunWith(ExampleGroupRunner.class)
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
    boolean _doubleArrow = Should.operator_doubleArrow(_first, IColorConstants.COMMENT);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\t#a comment\r\n\t\t\'\'\'.scan.first => COMMENT but"
     + "\n     \'\'\'\r\n\t\t#a comment\r\n\t\t\'\'\'.scan.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\r\n\t\t#a comment\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\t#a comment\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     COMMENT is " + new StringDescription().appendValue(IColorConstants.COMMENT).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_first, IColorConstants.ANNOTATION);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\t@a comment\r\n\t\t\'\'\'.scan.first => ANNOTATION but"
     + "\n     \'\'\'\r\n\t\t@a comment\r\n\t\t\'\'\'.scan.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\r\n\t\t@a comment\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\t@a comment\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     ANNOTATION is " + new StringDescription().appendValue(IColorConstants.ANNOTATION).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("parse comments until EOL")
  @Order(3)
  public void _parseCommentsUntilEOL() throws Exception {
    String _plus = ("#a comment" + Character.valueOf(AbstractRTextParser.EOL));
    List<RGB> _scan = this.scan(_plus);
    RGB _first = JnarioIterableExtensions.<RGB>first(_scan);
    boolean _doubleArrow = Should.operator_doubleArrow(_first, IColorConstants.COMMENT);
    Assert.assertTrue("\nExpected (\"#a comment\" + EOL).scan.first => COMMENT but"
     + "\n     (\"#a comment\" + EOL).scan.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     (\"#a comment\" + EOL).scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \"#a comment\" + EOL is " + new StringDescription().appendValue(_plus).toString()
     + "\n     EOL is " + new StringDescription().appendValue(Character.valueOf(AbstractRTextParser.EOL)).toString()
     + "\n     COMMENT is " + new StringDescription().appendValue(IColorConstants.COMMENT).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_first, IColorConstants.COMMAND);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.first => COMMAND but"
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     COMMAND is " + new StringDescription().appendValue(IColorConstants.COMMAND).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_first, IColorConstants.COMMAND);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType _name, label: /a/Reference\r\n\t\t\'\'\'.scan.first => COMMAND but"
     + "\n     \'\'\'\r\n\t\tType _name, label: /a/Reference\r\n\t\t\'\'\'.scan.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     \'\'\'\r\n\t\tType _name, label: /a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType _name, label: /a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     COMMAND is " + new StringDescription().appendValue(IColorConstants.COMMAND).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_second, IColorConstants.IDENTIFIER);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.second => IDENTIFIER but"
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.second is " + new StringDescription().appendValue(_second).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     IDENTIFIER is " + new StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_second, IColorConstants.IDENTIFIER);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name_with, label: /a/Reference\r\n\t\t\'\'\'.scan.second => IDENTIFIER but"
     + "\n     \'\'\'\r\n\t\tType name_with, label: /a/Reference\r\n\t\t\'\'\'.scan.second is " + new StringDescription().appendValue(_second).toString()
     + "\n     \'\'\'\r\n\t\tType name_with, label: /a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name_with, label: /a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     IDENTIFIER is " + new StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_third, IColorConstants.DEFAULT);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.third => DEFAULT but"
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.third is " + new StringDescription().appendValue(_third).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     DEFAULT is " + new StringDescription().appendValue(IColorConstants.DEFAULT).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.LABEL);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.get(5) => LABEL but"
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan.get(5) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     LABEL is " + new StringDescription().appendValue(IColorConstants.LABEL).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.REFERENCE);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: /long/a/Reference\r\n\t\t\'\'\'.scan.get(7) => REFERENCE but"
     + "\n     \'\'\'\r\n\t\tType name, label: /long/a/Reference\r\n\t\t\'\'\'.scan.get(7) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /long/a/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: /long/a/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     REFERENCE is " + new StringDescription().appendValue(IColorConstants.REFERENCE).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.GENERICS);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: <generic>\r\n\t\t\'\'\'.scan.get(7) => GENERICS but"
     + "\n     \'\'\'\r\n\t\tType name, label: <generic>\r\n\t\t\'\'\'.scan.get(7) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: <generic>\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: <generic>\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     GENERICS is " + new StringDescription().appendValue(IColorConstants.GENERICS).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.REFERENCE);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: a/long/Reference\r\n\t\t\'\'\'.scan.get(7) => REFERENCE but"
     + "\n     \'\'\'\r\n\t\tType name, label: a/long/Reference\r\n\t\t\'\'\'.scan.get(7) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: a/long/Reference\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: a/long/Reference\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     REFERENCE is " + new StringDescription().appendValue(IColorConstants.REFERENCE).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.NUMBER);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: 8\r\n\t\t\'\'\'.scan.get(7) => NUMBER but"
     + "\n     \'\'\'\r\n\t\tType name, label: 8\r\n\t\t\'\'\'.scan.get(7) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: 8\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: 8\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     NUMBER is " + new StringDescription().appendValue(IColorConstants.NUMBER).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.STRING);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: \"a string\"\r\n\t\t\'\'\'.scan.get(7) => STRING but"
     + "\n     \'\'\'\r\n\t\tType name, label: \"a string\"\r\n\t\t\'\'\'.scan.get(7) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: \"a string\"\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: \"a string\"\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     STRING is " + new StringDescription().appendValue(IColorConstants.STRING).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.IDENTIFIER);
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tType name, label: enum\r\n\t\t\'\'\'.scan.get(7) => IDENTIFIER but"
     + "\n     \'\'\'\r\n\t\tType name, label: enum\r\n\t\t\'\'\'.scan.get(7) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: enum\r\n\t\t\'\'\'.scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'\'\'\r\n\t\tType name, label: enum\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString()
     + "\n     IDENTIFIER is " + new StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("parse whitespace")
  @Order(16)
  public void _parseWhitespace() throws Exception {
    List<RGB> _scan = this.scan("Type name, label: enum\r\n");
    RGB _fifth = JnarioIterableExtensions.<RGB>fifth(_scan);
    boolean _doubleArrow = Should.operator_doubleArrow(_fifth, IColorConstants.IDENTIFIER);
    Assert.assertTrue("\nExpected \"Type name, label: enum\\r\\n\".scan.fifth => IDENTIFIER but"
     + "\n     \"Type name, label: enum\\r\\n\".scan.fifth is " + new StringDescription().appendValue(_fifth).toString()
     + "\n     \"Type name, label: enum\\r\\n\".scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     IDENTIFIER is " + new StringDescription().appendValue(IColorConstants.IDENTIFIER).toString() + "\n", _doubleArrow);
    
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
     + "\n     subject.tokenOffset + subject.tokenLength is " + new StringDescription().appendValue(_plus).toString()
     + "\n     subject.tokenOffset is " + new StringDescription().appendValue(_tokenOffset).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     subject.tokenLength is " + new StringDescription().appendValue(_tokenLength).toString()
     + "\n     input.length is " + new StringDescription().appendValue(_length).toString()
     + "\n     input is " + new StringDescription().appendValue(input).toString() + "\n", _lessEqualsThan);
    
  }
  
  @Test
  @Named("parse string until EOL")
  @Order(18)
  public void _parseStringUntilEOL() throws Exception {
    String _plus = ("Type name, label: \"a string " + Character.valueOf(AbstractRTextParser.EOL));
    List<RGB> _scan = this.scan(_plus);
    RGB _get = _scan.get(7);
    boolean _doubleArrow = Should.operator_doubleArrow(_get, IColorConstants.STRING);
    Assert.assertTrue("\nExpected (\'Type name, label: \"a string \' + EOL).scan.get(7) => STRING but"
     + "\n     \'Type name, label: \"a string \' + EOL).scan.get(7 is " + new StringDescription().appendValue(_get).toString()
     + "\n     (\'Type name, label: \"a string \' + EOL).scan is " + new StringDescription().appendValue(_scan).toString()
     + "\n     \'Type name, label: \"a string \' + EOL is " + new StringDescription().appendValue(_plus).toString()
     + "\n     EOL is " + new StringDescription().appendValue(Character.valueOf(AbstractRTextParser.EOL)).toString()
     + "\n     STRING is " + new StringDescription().appendValue(IColorConstants.STRING).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, "<source>");
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<source> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\'.regions.get(12) => \"<source>\" but"
     + "\n     \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<source> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\'.regions.get(12) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<source> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\'.regions is " + new StringDescription().appendValue(_regions).toString()
     + "\n     \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<source> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(_get, "<%text>text%>");
    Assert.assertTrue("\nExpected \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<%text>text%> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\'.regions.get(12) => \"<%text>text%>\" but"
     + "\n     \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<%text>text%> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\'.regions.get(12) is " + new StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<%text>text%> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\'.regions is " + new StringDescription().appendValue(_regions).toString()
     + "\n     \'\'\'\r\n\t\tEPackage{\r\n\t\t\tEClass{\r\n\t\t\t\tEOperation{\r\n\t\t\t\t\tEAnnotation source:<%text>text%> \r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t}\t\r\n\r\n\t\t\'\'\' is " + new StringDescription().appendValue(_builder).toString() + "\n", _doubleArrow);
    
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
    boolean _doubleArrow = Should.operator_doubleArrow(region, "<%text>text");
    Assert.assertTrue("\nExpected region => \"<%text>text\" but"
     + "\n     region is " + new StringDescription().appendValue(region).toString() + "\n", _doubleArrow);
    
  }
  
  public List<String> regions(final CharSequence s) {
    List<String> _xblockexpression = null;
    {
      String _string = s.toString();
      SimpleDocument _simpleDocument = new SimpleDocument(_string);
      final SimpleDocument document = _simpleDocument;
      int _length = s.length();
      this.subject.setRange(document, 0, _length);
      final List<String> regions = JnarioCollectionLiterals.<String>list();
      IToken token = this.subject.nextToken();
      boolean _isEOF = token.isEOF();
      boolean _not = (!_isEOF);
      boolean _while = _not;
      while (_while) {
        {
          int _tokenOffset = this.subject.getTokenOffset();
          int _tokenLength = this.subject.getTokenLength();
          String _get = document.get(_tokenOffset, _tokenLength);
          regions.add(_get);
          IToken _nextToken = this.subject.nextToken();
          token = _nextToken;
        }
        boolean _isEOF_1 = token.isEOF();
        boolean _not_1 = (!_isEOF_1);
        _while = _not_1;
      }
      _xblockexpression = (regions);
    }
    return _xblockexpression;
  }
  
  public List<RGB> scan(final CharSequence s) {
    List<RGB> _xblockexpression = null;
    {
      String _string = s.toString();
      SimpleDocument _simpleDocument = new SimpleDocument(_string);
      final SimpleDocument document = _simpleDocument;
      int _length = s.length();
      this.subject.setRange(document, 0, _length);
      final List<IToken> tokens = JnarioCollectionLiterals.<IToken>list();
      IToken token = this.subject.nextToken();
      boolean _isEOF = token.isEOF();
      boolean _not = (!_isEOF);
      boolean _while = _not;
      while (_while) {
        {
          tokens.add(token);
          IToken _nextToken = this.subject.nextToken();
          token = _nextToken;
        }
        boolean _isEOF_1 = token.isEOF();
        boolean _not_1 = (!_isEOF_1);
        _while = _not_1;
      }
      final Function1<IToken,RGB> _function = new Function1<IToken,RGB>() {
          public RGB apply(final IToken it) {
            RGB _xblockexpression = null;
            {
              Object _data = it.getData();
              final TextAttribute attr = ((TextAttribute) _data);
              Color _foreground = attr.getForeground();
              RGB _rGB = _foreground.getRGB();
              _xblockexpression = (_rGB);
            }
            return _xblockexpression;
          }
        };
      List<RGB> _map = ListExtensions.<IToken, RGB>map(tokens, _function);
      _xblockexpression = (_map);
    }
    return _xblockexpression;
  }
}
