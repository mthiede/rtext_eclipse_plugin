package org.rtext.lang.specs.unit.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ContextParser;
import org.rtext.lang.backend.DocumentContext;
import org.rtext.lang.specs.util.SimpleDocument;

@Named("ContextParser")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ContextParserSpec {
  @Test
  @Named("test_simple")
  @Order(1)
  public void _testSimple() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("|F bla");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("C a1: v1, a2: \"v2\"");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("D {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("E a1: 5");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("|F bla");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_child_label")
  @Order(2)
  public void _testChildLabel() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("sub:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("F bla|");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("sub:");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("C a1: v1, a2: \"v2\"");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("D {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("E a1: 5");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("F bla|");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_child_label_array")
  @Order(3)
  public void _testChildLabelArray() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("sub: [");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("F| bla");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("sub: [");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("C");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("C a1: v1, a2: \"v2\"");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("D {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("E a1: 5");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("F| bla");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_ignore_child_lables")
  @Order(4)
  public void _testIgnoreChildLables() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("F bl|a");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("sub:");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("C a1: v1, a2: \"v2\"");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("sub2: [");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("D {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("E a1: 5");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("]");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("F bl|a");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_linebreak")
  @Order(5)
  public void _testLinebreak() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("C name,a1: v1,a2: \"v2\"|");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("C name,");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("a1: v1,");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("a2: \"v2\"|");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_linebreak_arg_array")
  @Order(6)
  public void _testLinebreakArgArray() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("C name,a1: [v1,v2],a2: |5");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("C name,");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("a1: [   ");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("v1,");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("v2");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("], ");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("a2: |5");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_linebreak_empty_last_line")
  @Order(7)
  public void _testLinebreakEmptyLastLine() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B name,|");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B name,");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("|");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_linebreak_empty_last_line2")
  @Order(8)
  public void _testLinebreakEmptyLastLine2() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B name,|");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B name,");
    _builder_1.newLine();
    _builder_1.append(" ");
    _builder_1.append("|");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_linebreak_empty_lines")
  @Order(9)
  public void _testLinebreakEmptyLines() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B name,a1: |");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B name, ");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("a1: |");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_comment_annotation")
  @Order(10)
  public void _testCommentAnnotation() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("A {");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("B {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("|F bla");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("A {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("# bla");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("B {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("C a1: v1, a2: \"v2\"");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("# bla");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("D {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("E a1: 5");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("@ anno");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("|F bla");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  @Test
  @Named("test_line_break")
  @Order(11)
  public void _testLineBreak() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("EPackage StatemachineMM,  cnsURI: \"\",|");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("EPackage StatemachineMM,  cnsURI: \"\", ");
    _builder_1.newLine();
    _builder_1.append("|");
    _builder_1.newLine();
    this.assert_context(_builder.toString(), _builder_1.toString());
  }
  
  public void assert_context(final String expected, final String actual) {
    String[] _split = expected.split("\n");
    final ArrayList<String> exp_lines = new ArrayList<String>((Collection<? extends String>)Conversions.doWrapArray(_split));
    String _last = IterableExtensions.<String>last(exp_lines);
    int _indexOf = _last.indexOf("|");
    final int exp_col = (_indexOf + 1);
    int _length = ((Object[])Conversions.unwrapArray(exp_lines, Object.class)).length;
    int _minus = (_length - 1);
    String _remove = exp_lines.remove(_minus);
    String _replace = _remove.replace("|", "");
    exp_lines.add(_replace);
    String _trim = actual.trim();
    String _replace_1 = _trim.replace("|", "");
    int _indexOf_1 = actual.indexOf("|");
    final DocumentContext ctx = this.parse(_replace_1, _indexOf_1);
    List<String> _parseContext = ctx.getParseContext();
    Assert.assertEquals(exp_lines, _parseContext);
    int _column = ctx.getColumn();
    Assert.assertEquals(exp_col, _column);
  }
  
  public DocumentContext parse(final String text, final int col) {
    DocumentContext _xblockexpression = null;
    {
      final SimpleDocument document = new SimpleDocument(text);
      final ContextParser parser = new ContextParser(document);
      _xblockexpression = parser.getContext(col);
    }
    return _xblockexpression;
  }
}
