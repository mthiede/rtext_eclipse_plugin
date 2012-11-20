package org.rtext.lang.specs.unit.backend;

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
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ConnectorConfigProvider;
import org.rtext.lang.backend2.ConnectorFactory;
import org.rtext.lang.backend2.DefaultConnectorProvider;
import org.rtext.lang.specs.util.MockInjector;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("DefaultConnectorProvider")
@CreateWith(value = MockInjector.class)
public class DefaultConnectorProviderSpec {
  public DefaultConnectorProvider subject;
  
  @Mock
  ConnectorConfigProvider configFileProvider;
  
  @Mock
  ConnectorConfig config;
  
  @Mock
  Connector connector;
  
  @Mock
  ConnectorFactory connectorFactory;
  
  String aModelPath = "a path";
  
  String anotherModelPath = "another path";
  
  @Before
  public void before() throws Exception {
    DefaultConnectorProvider _defaultConnectorProvider = new DefaultConnectorProvider(this.configFileProvider, this.connectorFactory);
    this.subject = _defaultConnectorProvider;
    Connector _createConnector = this.connectorFactory.createConnector(this.config);
    OngoingStubbing<Connector> _when = Mockito.<Connector>when(_createConnector);
    _when.thenReturn(this.connector);
    String _anyString = Matchers.anyString();
    ConnectorConfig _get = this.configFileProvider.get(_anyString);
    OngoingStubbing<ConnectorConfig> _when_1 = Mockito.<ConnectorConfig>when(_get);
    _when_1.thenReturn(this.config);
  }
  
  @Test
  @Named("Creates connector with file specific configuration")
  @Order(1)
  public void _createsConnectorWithFileSpecificConfiguration() throws Exception {
    Connector _get = this.subject.get(this.aModelPath);
    boolean _doubleArrow = Should.operator_doubleArrow(_get, Connector.class);
    Assert.assertTrue("\nExpected subject.get(aModelPath) => typeof(Connector) but"
     + "\n     subject.get(aModelPath) is " + new StringDescription().appendValue(_get).toString()
     + "\n     subject is " + new StringDescription().appendValue(this.subject).toString()
     + "\n     aModelPath is " + new StringDescription().appendValue(this.aModelPath).toString() + "\n", _doubleArrow);
    
    ConnectorConfigProvider _verify = Mockito.<ConnectorConfigProvider>verify(this.configFileProvider);
    _verify.get(this.aModelPath);
  }
  
  @Test
  @Named("disposes all connectors")
  @Order(2)
  public void _disposesAllConnectors() throws Exception {
    this.subject.get(this.aModelPath);
    this.subject.dispose();
    Connector _verify = Mockito.<Connector>verify(this.connector);
    _verify.dispose();
  }
}
