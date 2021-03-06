package org.rtext.lang.specs.unit.backend;

import com.google.common.base.Objects;
import java.io.File;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.lib.Assert;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.runner.Subject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.specs.util.Files;

@Named("RTextFileParser")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class RTextFileParserSpec {
  @Subject
  public RTextFileParser subject;
  
  @Rule
  @Extension
  @org.jnario.runner.Extension
  public TemporaryFolder _temporaryFolder = new TemporaryFolder();
  
  @Test
  @Named("File specific commands are defined by: \\\'FILE_PATTERN: COMMAND\\\'")
  @Order(1)
  public void _fileSpecificCommandsAreDefinedByFILEPATTERNCOMMAND() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("*.ect:");
    _builder.newLine();
    _builder.append("the command");
    _builder.newLine();
    RTextFile _parse = this.parse(_builder);
    final Procedure1<RTextFile> _function = new Procedure1<RTextFile>() {
      public void apply(final RTextFile it) {
        Pair<String, String> _mappedTo = Pair.<String, String>of("ect", "the command");
        boolean _should_contain = RTextFileParserSpec.this.should_contain(it, _mappedTo);
        Assert.assertTrue("\nExpected it should contain \"ect\" -> \"the command\" but"
         + "\n     it is " + new org.hamcrest.StringDescription().appendValue(it).toString()
         + "\n     \"ect\" -> \"the command\" is " + new org.hamcrest.StringDescription().appendValue(_mappedTo).toString() + "\n", _should_contain);
        
        Pair<String, String> _mappedTo_1 = Pair.<String, String>of("unknown", "");
        Assert.assertFalse("\nExpected it should not contain \"unknown\" -> \"\" but"
         + "\n     it is " + new org.hamcrest.StringDescription().appendValue(it).toString()
         + "\n     \"unknown\" -> \"\" is " + new org.hamcrest.StringDescription().appendValue(_mappedTo_1).toString() + "\n", RTextFileParserSpec.this.should_contain(it, _mappedTo_1));
        
      }
    };
    ObjectExtensions.<RTextFile>operator_doubleArrow(_parse, _function);
  }
  
  @Test
  @Named("Multiple commands are separated by newlines")
  @Order(2)
  public void _multipleCommandsAreSeparatedByNewlines() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("*.ect:");
    _builder.newLine();
    _builder.append("the command");
    _builder.newLine();
    _builder.append("*.abc:");
    _builder.newLine();
    _builder.append("other command");
    _builder.newLine();
    RTextFile _parse = this.parse(_builder);
    final Procedure1<RTextFile> _function = new Procedure1<RTextFile>() {
      public void apply(final RTextFile it) {
        Pair<String, String> _mappedTo = Pair.<String, String>of("ect", "the command");
        boolean _should_contain = RTextFileParserSpec.this.should_contain(it, _mappedTo);
        Assert.assertTrue("\nExpected it should contain \"ect\" -> \"the command\" but"
         + "\n     it is " + new org.hamcrest.StringDescription().appendValue(it).toString()
         + "\n     \"ect\" -> \"the command\" is " + new org.hamcrest.StringDescription().appendValue(_mappedTo).toString() + "\n", _should_contain);
        
        Pair<String, String> _mappedTo_1 = Pair.<String, String>of("abc", "other command");
        Assert.assertTrue("\nExpected it should contain \"abc\" -> \"other command\" but"
         + "\n     it is " + new org.hamcrest.StringDescription().appendValue(it).toString()
         + "\n     \"abc\" -> \"other command\" is " + new org.hamcrest.StringDescription().appendValue(_mappedTo_1).toString() + "\n", RTextFileParserSpec.this.should_contain(it, _mappedTo_1));
        
      }
    };
    ObjectExtensions.<RTextFile>operator_doubleArrow(_parse, _function);
  }
  
  @Test
  @Named("Handles whitespace at the end")
  @Order(3)
  public void _handlesWhitespaceAtTheEnd() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("*.ect: ");
    _builder.newLine();
    _builder.append("the command");
    _builder.newLine();
    RTextFile _parse = this.parse(_builder);
    final Procedure1<RTextFile> _function = new Procedure1<RTextFile>() {
      public void apply(final RTextFile it) {
        Pair<String, String> _mappedTo = Pair.<String, String>of("ect", "the command");
        Assert.assertTrue("\nExpected it should contain \"ect\" -> \"the command\" but"
         + "\n     it is " + new org.hamcrest.StringDescription().appendValue(it).toString()
         + "\n     \"ect\" -> \"the command\" is " + new org.hamcrest.StringDescription().appendValue(_mappedTo).toString() + "\n", RTextFileParserSpec.this.should_contain(it, _mappedTo));
        
      }
    };
    ObjectExtensions.<RTextFile>operator_doubleArrow(_parse, _function);
  }
  
  public boolean should_contain(final RTextFile file, final Pair<String, String> command) {
    String _key = command.getKey();
    String _plus = ("*." + _key);
    final ConnectorConfig etc = file.getConfiguration(_plus);
    boolean _equals = Objects.equal(etc, null);
    if (_equals) {
      return false;
    }
    String _command = etc.getCommand();
    String _value = command.getValue();
    return Objects.equal(_command, _value);
  }
  
  public RTextFile parse(final CharSequence s) {
    try {
      File _newFile = this._temporaryFolder.newFile("input.txt");
      File _newFileWithContent = Files.newFileWithContent(_newFile, s);
      return this.subject.doParse(_newFileWithContent);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
