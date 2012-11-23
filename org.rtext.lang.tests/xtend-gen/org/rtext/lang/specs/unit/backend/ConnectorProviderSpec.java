package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.CachingConnectorProvider;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorConfigProvider;
import org.rtext.lang.backend.ConnectorFactory;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.specs.util.MockInjector;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("ConnectorProvider")
@CreateWith(value = MockInjector.class)
public class ConnectorProviderSpec {
  public ConnectorProvider subject;
  
  @Mock
  ConnectorConfigProvider configFileProvider;
  
  ConnectorConfig config = new Function0<ConnectorConfig>() {
    public ConnectorConfig apply() {
      File _file = new File("test1/.rtext");
      ConnectorConfig _connectorConfig = new ConnectorConfig(_file, "");
      return _connectorConfig;
    }
  }.apply();
  
  ConnectorConfig anotherConfig = new Function0<ConnectorConfig>() {
    public ConnectorConfig apply() {
      File _file = new File("test2/.rtext");
      ConnectorConfig _connectorConfig = new ConnectorConfig(_file, "");
      return _connectorConfig;
    }
  }.apply();
  
  @Mock
  ConnectorFactory connectorFactory;
  
  @Mock
  Connector connector;
  
  @Mock
  Connector anotherConnector;
  
  String aModelPath = "a path";
  
  String anotherModelPath = "another path";
  
  @Before
  public void before() throws Exception {
    CachingConnectorProvider _cachingConnectorProvider = new CachingConnectorProvider(this.configFileProvider, this.connectorFactory);
    this.subject = _cachingConnectorProvider;
    Connector _createConnector = this.connectorFactory.createConnector(this.config);
    OngoingStubbing<Connector> _when = Mockito.<Connector>when(_createConnector);
    _when.thenReturn(this.connector);
    Connector _createConnector_1 = this.connectorFactory.createConnector(this.anotherConfig);
    OngoingStubbing<Connector> _when_1 = Mockito.<Connector>when(_createConnector_1);
    _when_1.thenReturn(this.anotherConnector);
  }
  
  @Test
  @Named("Creates connector with file specific configuration")
  @Order(1)
  public void _createsConnectorWithFileSpecificConfiguration() throws Exception {
    String _anyString = Matchers.anyString();
    ConnectorConfig _get = this.configFileProvider.get(_anyString);
    OngoingStubbing<ConnectorConfig> _when = Mockito.<ConnectorConfig>when(_get);
    _when.thenReturn(this.config);
    Connector _get_1 = this.subject.get(this.aModelPath);
    boolean _doubleArrow = Should.operator_doubleArrow(_get_1, Connector.class);
    Assert.assertTrue("\nExpected subject.get(aModelPath) => typeof(Connector) but"
     + "\n     subject.get(aModelPath) is " + new StringDescription().appendValue(_get_1).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     aModelPath is " + new StringDescription().appendValue(this.aModelPath).toString() + "\n", _doubleArrow);
    
    ConnectorConfigProvider _verify = Mockito.<ConnectorConfigProvider>verify(this.configFileProvider);
    _verify.get(this.aModelPath);
  }
  
  @Test
  @Named("returns same connector for files with same configuration")
  @Order(2)
  public void _returnsSameConnectorForFilesWithSameConfiguration() throws Exception {
    String _anyString = Matchers.anyString();
    ConnectorConfig _get = this.configFileProvider.get(_anyString);
    OngoingStubbing<ConnectorConfig> _when = Mockito.<ConnectorConfig>when(_get);
    _when.thenReturn(this.config);
    Connector _get_1 = this.subject.get(this.aModelPath);
    Connector _get_2 = this.subject.get(this.anotherModelPath);
    boolean _should_be = Should.should_be(_get_1, _get_2);
    Assert.assertTrue("\nExpected subject.get(aModelPath) should be subject.get(anotherModelPath) but"
     + "\n     subject.get(aModelPath) is " + new StringDescription().appendValue(_get_1).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     aModelPath is " + new StringDescription().appendValue(this.aModelPath).toString()
     + "\n     subject.get(anotherModelPath) is " + new StringDescription().appendValue(_get_2).toString()
     + "\n     anotherModelPath is " + new StringDescription().appendValue(this.anotherModelPath).toString() + "\n", _should_be);
    
  }
  
  @Test
  @Named("returns different connector for files with different configuration")
  @Order(3)
  public void _returnsDifferentConnectorForFilesWithDifferentConfiguration() throws Exception {
    ConnectorConfig _get = this.configFileProvider.get(this.aModelPath);
    OngoingStubbing<ConnectorConfig> _when = Mockito.<ConnectorConfig>when(_get);
    _when.thenReturn(this.config);
    ConnectorConfig _get_1 = this.configFileProvider.get(this.anotherModelPath);
    OngoingStubbing<ConnectorConfig> _when_1 = Mockito.<ConnectorConfig>when(_get_1);
    _when_1.thenReturn(this.anotherConfig);
    Connector _get_2 = this.subject.get(this.aModelPath);
    Connector _get_3 = this.subject.get(this.anotherModelPath);
    boolean _should_be = Should.should_be(_get_2, _get_3);
    Assert.assertFalse("\nExpected subject.get(aModelPath) should not be subject.get(anotherModelPath) but"
     + "\n     subject.get(aModelPath) is " + new StringDescription().appendValue(_get_2).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     aModelPath is " + new StringDescription().appendValue(this.aModelPath).toString()
     + "\n     subject.get(anotherModelPath) is " + new StringDescription().appendValue(_get_3).toString()
     + "\n     anotherModelPath is " + new StringDescription().appendValue(this.anotherModelPath).toString() + "\n", _should_be);
    
  }
  
  @Test
  @Named("returns null if config file cannot be resolved")
  @Order(4)
  public void _returnsNullIfConfigFileCannotBeResolved() throws Exception {
    String _anyString = Matchers.anyString();
    ConnectorConfig _get = this.configFileProvider.get(_anyString);
    OngoingStubbing<ConnectorConfig> _when = Mockito.<ConnectorConfig>when(_get);
    _when.thenReturn(null);
    Connector _get_1 = this.subject.get(this.aModelPath);
    Matcher<Connector> _nullValue = CoreMatchers.<Connector>nullValue();
    boolean _should_be = Should.<Connector>should_be(_get_1, _nullValue);
    Assert.assertTrue("\nExpected subject.get(aModelPath) should be null but"
     + "\n     subject.get(aModelPath) is " + new StringDescription().appendValue(_get_1).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     aModelPath is " + new StringDescription().appendValue(this.aModelPath).toString() + "\n", _should_be);
    
  }
  
  @Test
  @Named("disposes all connectors for given config file")
  @Order(5)
  public void _disposesAllConnectorsForGivenConfigFile() throws Exception {
    ConnectorConfig _get = this.configFileProvider.get(this.aModelPath);
    OngoingStubbing<ConnectorConfig> _when = Mockito.<ConnectorConfig>when(_get);
    _when.thenReturn(this.config);
    this.subject.get(this.aModelPath);
    File _configFile = this.config.getConfigFile();
    String _path = _configFile.getPath();
    this.subject.dispose(_path);
    Connector _verify = Mockito.<Connector>verify(this.connector);
    _verify.dispose();
  }
}
