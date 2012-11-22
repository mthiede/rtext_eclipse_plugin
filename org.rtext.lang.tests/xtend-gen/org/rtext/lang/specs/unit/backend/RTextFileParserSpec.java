package org.rtext.lang.specs.unit.backend;

import com.google.common.base.Objects;
import java.io.File;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.jnario.runner.Subject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.backend2.RTextFile;
import org.rtext.lang.specs.util.Files;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("RTextFileParser")
public class RTextFileParserSpec {
  @Subject
  public RTextFileParser subject;
  
  @Rule
  @Extension
  public TemporaryFolder _temporaryFolder = new Function0<TemporaryFolder>() {
    public TemporaryFolder apply() {
      TemporaryFolder _temporaryFolder = new TemporaryFolder();
      return _temporaryFolder;
    }
  }.apply();
  
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
          Pair<String,String> _mappedTo = Pair.<String, String>of("ect", "the command");
          boolean _should_contain = RTextFileParserSpec.this.should_contain(it, _mappedTo);
          Assert.assertTrue("\nExpected it should contain \"ect\" -> \"the command\" but"
           + "\n     it is " + new StringDescription().appendValue(it).toString()
           + "\n     \"ect\" -> \"the command\" is " + new StringDescription().appendValue(_mappedTo).toString() + "\n", _should_contain);
          
          Pair<String,String> _mappedTo_1 = Pair.<String, String>of("unknown", "");
          boolean _should_contain_1 = RTextFileParserSpec.this.should_contain(it, _mappedTo_1);
          Assert.assertFalse("\nExpected it should not contain \"unknown\" -> \"\" but"
           + "\n     it is " + new StringDescription().appendValue(it).toString()
           + "\n     \"unknown\" -> \"\" is " + new StringDescription().appendValue(_mappedTo_1).toString() + "\n", _should_contain_1);
          
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
          Pair<String,String> _mappedTo = Pair.<String, String>of("ect", "the command");
          boolean _should_contain = RTextFileParserSpec.this.should_contain(it, _mappedTo);
          Assert.assertTrue("\nExpected it should contain \"ect\" -> \"the command\" but"
           + "\n     it is " + new StringDescription().appendValue(it).toString()
           + "\n     \"ect\" -> \"the command\" is " + new StringDescription().appendValue(_mappedTo).toString() + "\n", _should_contain);
          
          Pair<String,String> _mappedTo_1 = Pair.<String, String>of("abc", "other command");
          boolean _should_contain_1 = RTextFileParserSpec.this.should_contain(it, _mappedTo_1);
          Assert.assertTrue("\nExpected it should contain \"abc\" -> \"other command\" but"
           + "\n     it is " + new StringDescription().appendValue(it).toString()
           + "\n     \"abc\" -> \"other command\" is " + new StringDescription().appendValue(_mappedTo_1).toString() + "\n", _should_contain_1);
          
        }
      };
    ObjectExtensions.<RTextFile>operator_doubleArrow(_parse, _function);
  }
  
  public boolean should_contain(final RTextFile file, final Pair<String,String> command) {
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
      RTextFile _doParse = this.subject.doParse(_newFileWithContent);
      return _doParse;
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
