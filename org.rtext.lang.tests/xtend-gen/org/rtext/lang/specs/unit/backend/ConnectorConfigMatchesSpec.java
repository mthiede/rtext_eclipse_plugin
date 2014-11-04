package org.rtext.lang.specs.unit.backend;

import com.google.common.collect.Iterables;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.lib.Assert;
import org.jnario.lib.ExampleTable;
import org.jnario.lib.ExampleTableIterators;
import org.jnario.lib.JnarioCollectionLiterals;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.specs.unit.backend.ConnectorConfigMatchesSpecExamples;
import org.rtext.lang.specs.unit.backend.ConnectorConfigSpec;

@Named("matches")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class ConnectorConfigMatchesSpec extends ConnectorConfigSpec {
  public ExampleTable<ConnectorConfigMatchesSpecExamples> _initConnectorConfigMatchesSpecExamples() {
    return ExampleTable.create("examples", 
      Arrays.asList("fileName", "patterns", "doesMatch"), 
      new ConnectorConfigMatchesSpecExamples(  Arrays.asList("\"a.txt\"", "list(\"*.txt\")", "true"), _initConnectorConfigMatchesSpecExamplesCell0(), _initConnectorConfigMatchesSpecExamplesCell1(), _initConnectorConfigMatchesSpecExamplesCell2()),
      new ConnectorConfigMatchesSpecExamples(  Arrays.asList("\"a.txt1\"", "list(\"*.txt\")", "false"), _initConnectorConfigMatchesSpecExamplesCell3(), _initConnectorConfigMatchesSpecExamplesCell4(), _initConnectorConfigMatchesSpecExamplesCell5()),
      new ConnectorConfigMatchesSpecExamples(  Arrays.asList("\"a.txt\"", "list(\"*.txt\")", "true"), _initConnectorConfigMatchesSpecExamplesCell6(), _initConnectorConfigMatchesSpecExamplesCell7(), _initConnectorConfigMatchesSpecExamplesCell8())
    );
  }
  
  protected ExampleTable<ConnectorConfigMatchesSpecExamples> examples = _initConnectorConfigMatchesSpecExamples();
  
  public String _initConnectorConfigMatchesSpecExamplesCell0() {
    return "a.txt";
  }
  
  public List<String> _initConnectorConfigMatchesSpecExamplesCell1() {
    List<String> _list = JnarioCollectionLiterals.<String>list("*.txt");
    return _list;
  }
  
  public boolean _initConnectorConfigMatchesSpecExamplesCell2() {
    return true;
  }
  
  public String _initConnectorConfigMatchesSpecExamplesCell3() {
    return "a.txt1";
  }
  
  public List<String> _initConnectorConfigMatchesSpecExamplesCell4() {
    List<String> _list = JnarioCollectionLiterals.<String>list("*.txt");
    return _list;
  }
  
  public boolean _initConnectorConfigMatchesSpecExamplesCell5() {
    return false;
  }
  
  public String _initConnectorConfigMatchesSpecExamplesCell6() {
    return "a.txt";
  }
  
  public List<String> _initConnectorConfigMatchesSpecExamplesCell7() {
    List<String> _list = JnarioCollectionLiterals.<String>list("*.txt");
    return _list;
  }
  
  public boolean _initConnectorConfigMatchesSpecExamplesCell8() {
    return true;
  }
  
  @Test
  @Named("examples.forEach[ val config = new ConnectorConfig[ _, \\\"anyCommand\\\", patterns.toArray[typeof[String]] ] config.matches[fileName] => doesMatch ]")
  @Order(1)
  public void _examplesForEachValConfigNewConnectorConfigAnyCommandPatternsToArrayTypeofStringConfigMatchesFileNameDoesMatch() throws Exception {
    final Procedure1<ConnectorConfigMatchesSpecExamples> _function = new Procedure1<ConnectorConfigMatchesSpecExamples>() {
      public void apply(final ConnectorConfigMatchesSpecExamples it) {
        File __ = Should.<File>_();
        List<String> _patterns = it.getPatterns();
        String[] _array = Iterables.<String>toArray(_patterns, String.class);
        final ConnectorConfig config = new ConnectorConfig(__, 
          "anyCommand", _array);
        String _fileName = it.getFileName();
        boolean _matches = config.matches(_fileName);
        boolean _doesMatch = it.getDoesMatch();
        Assert.assertTrue("\nExpected config.matches(fileName) => doesMatch but"
         + "\n     config.matches(fileName) is " + new org.hamcrest.StringDescription().appendValue(Boolean.valueOf(_matches)).toString()
         + "\n     config is " + new org.hamcrest.StringDescription().appendValue(config).toString()
         + "\n     fileName is " + new org.hamcrest.StringDescription().appendValue(_fileName).toString()
         + "\n     doesMatch is " + new org.hamcrest.StringDescription().appendValue(_doesMatch).toString() + "\n", Should.<Boolean>operator_doubleArrow(Boolean.valueOf(_matches), _doesMatch));
        
      }
    };
    ExampleTableIterators.<ConnectorConfigMatchesSpecExamples>forEach(this.examples, _function);
  }
}
