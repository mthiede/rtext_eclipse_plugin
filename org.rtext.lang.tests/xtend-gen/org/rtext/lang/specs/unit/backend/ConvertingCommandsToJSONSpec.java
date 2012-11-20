package org.rtext.lang.specs.unit.backend;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.CommandSerializer;
import org.rtext.lang.backend2.Response;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Converting Commands to JSON")
public class ConvertingCommandsToJSONSpec {
  final String command = "load_model";
  
  final String type = "request";
  
  final int invocationId = 42;
  
  @Extension
  public CommandSerializer serializer = new Function0<CommandSerializer>() {
    public CommandSerializer apply() {
      CommandSerializer _commandSerializer = new CommandSerializer();
      return _commandSerializer;
    }
  }.apply();
  
  @Test
  @Named("converts command to json")
  @Order(1)
  public void _convertsCommandToJson() throws Exception {
    Command<Response> _command = new Command<Response>(this.invocationId, this.type, this.command, Response.class);
    String _serialize = this.serializer.serialize(_command);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\"type\": \"");
    _builder.append(this.type, "    ");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("\"command\": \"");
    _builder.append(this.command, "    ");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("\"invocation_id\": ");
    _builder.append(this.invocationId, "    ");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    this.is(_serialize, _builder);
  }
  
  public void is(final String actual, final CharSequence expected) {
    String _string = expected.toString();
    String text = _string.replaceAll("\\s", "");
    int _length = text.length();
    String _plus = (Integer.valueOf(_length) + text);
    text = _plus;
    Assert.assertEquals(text, actual);
  }
}
