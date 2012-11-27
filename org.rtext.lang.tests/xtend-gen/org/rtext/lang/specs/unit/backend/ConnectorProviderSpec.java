package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.CachingConnectorProvider;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorFactory;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.specs.util.MockInjector;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("ConnectorProvider")
@CreateWith(value = MockInjector.class)
public class ConnectorProviderSpec {
  public ConnectorProvider subject;
  
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
    CachingConnectorProvider _cachingConnectorProvider = new CachingConnectorProvider(this.connectorFactory);
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
    Connector _get = this.subject.get(this.config);
    boolean _doubleArrow = Should.operator_doubleArrow(_get, Connector.class);
    Assert.assertTrue("\nExpected subject.get(config) => typeof(Connector) but"
     + "\n     subject.get(config) is " + new StringDescription().appendValue(_get).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     config is " + new StringDescription().appendValue(this.config).toString() + "\n", _doubleArrow);
    
    ConnectorFactory _verify = Mockito.<ConnectorFactory>verify(this.connectorFactory);
    _verify.createConnector(this.config);
  }
  
  @Test
  @Named("returns same connector for same configuration")
  @Order(2)
  public void _returnsSameConnectorForSameConfiguration() throws Exception {
    Connector _get = this.subject.get(this.config);
    Connector _get_1 = this.subject.get(this.config);
    boolean _should_be = Should.should_be(_get, _get_1);
    Assert.assertTrue("\nExpected subject.get(config) should be subject.get(config) but"
     + "\n     subject.get(config) is " + new StringDescription().appendValue(_get).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     config is " + new StringDescription().appendValue(this.config).toString() + "\n", _should_be);
    
  }
  
  @Test
  @Named("returns different connector for different configuration")
  @Order(3)
  public void _returnsDifferentConnectorForDifferentConfiguration() throws Exception {
    Connector _get = this.subject.get(this.config);
    Connector _get_1 = this.subject.get(this.anotherConfig);
    boolean _should_be = Should.should_be(_get, _get_1);
    Assert.assertFalse("\nExpected subject.get(config) should not be subject.get(anotherConfig) but"
     + "\n     subject.get(config) is " + new StringDescription().appendValue(_get).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     config is " + new StringDescription().appendValue(this.config).toString()
     + "\n     subject.get(anotherConfig) is " + new StringDescription().appendValue(_get_1).toString()
     + "\n     anotherConfig is " + new StringDescription().appendValue(this.anotherConfig).toString() + "\n", _should_be);
    
  }
  
  @Test
  @Named("disposes all connectors for given config file")
  @Order(4)
  public void _disposesAllConnectorsForGivenConfigFile() throws Exception {
    this.subject.get(this.config);
    File _configFile = this.config.getConfigFile();
    String _path = _configFile.getPath();
    this.subject.dispose(_path);
    Connector _verify = Mockito.<Connector>verify(this.connector);
    _verify.disconnect();
  }
}
