package org.rtext.lang.specs.unit.backend;

import com.google.common.collect.Iterables;
import java.io.File;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.ExampleTable;
import org.jnario.lib.ExampleTableIterators;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.specs.unit.backend.ConnectorConfigMatchesSpecExamples;
import org.rtext.lang.specs.unit.backend.ConnectorConfigSpec;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("matches")
public class ConnectorConfigMatchesSpec extends ConnectorConfigSpec {
  public ExampleTable<ConnectorConfigMatchesSpecExamples> _initConnectorConfigMatchesSpecExamples() {
    
    List<String> _list = JnarioCollectionLiterals.<String>list("*.txt");
    List<String> _list_1 = JnarioCollectionLiterals.<String>list("*.txt");
    List<String> _list_2 = JnarioCollectionLiterals.<String>list("*.txt");return ExampleTable.create("examples", 
      java.util.Arrays.asList("fileName", "patterns", "doesMatch"), 
      new ConnectorConfigMatchesSpecExamples(  java.util.Arrays.asList("\"a.txt\"", "list(\"*.txt\")", "true"), "a.txt", _list, true),
      new ConnectorConfigMatchesSpecExamples(  java.util.Arrays.asList("\"a.txt1\"", "list(\"*.txt\")", "false"), "a.txt1", _list_1, false),
      new ConnectorConfigMatchesSpecExamples(  java.util.Arrays.asList("\"a.txt\"", "list(\"*.txt\")", "true"), "a.txt", _list_2, true)
    );
  }
  
  protected ExampleTable<ConnectorConfigMatchesSpecExamples> examples = _initConnectorConfigMatchesSpecExamples();
  
  @Test
  @Named("examples.forEach[ val config = new ConnectorConfig[ _, \\\"anyCommand\\\", patterns.toArray[typeof[String]] ] config.matches[fileName] => doesMatch ]")
  @Order(1)
  public void _examplesForEachValConfigNewConnectorConfigAnyCommandPatternsToArrayTypeofStringConfigMatchesFileNameDoesMatch() throws Exception {
    final Procedure1<ConnectorConfigMatchesSpecExamples> _function = new Procedure1<ConnectorConfigMatchesSpecExamples>() {
        public void apply(final ConnectorConfigMatchesSpecExamples it) {
          File __ = Should.<File>_();
          String[] _array = Iterables.<String>toArray(it.patterns, String.class);
          ConnectorConfig _connectorConfig = new ConnectorConfig(__, 
            "anyCommand", _array);
          final ConnectorConfig config = _connectorConfig;
          boolean _matches = config.matches(it.fileName);
          boolean _doubleArrow = Should.operator_doubleArrow(Boolean.valueOf(_matches), Boolean.valueOf(it.doesMatch));
          Assert.assertTrue("\nExpected config.matches(fileName) => doesMatch but"
           + "\n     config.matches(fileName) is " + new StringDescription().appendValue(Boolean.valueOf(_matches)).toString()
           + "\n     config is " + new StringDescription().appendValue(config).toString()
           + "\n     fileName is " + new StringDescription().appendValue(it.fileName).toString()
           + "\n     doesMatch is " + new StringDescription().appendValue(Boolean.valueOf(it.doesMatch)).toString() + "\n", _doubleArrow);
          
        }
      };
    ExampleTableIterators.<ConnectorConfigMatchesSpecExamples>forEach(this.examples, _function);
  }
}
