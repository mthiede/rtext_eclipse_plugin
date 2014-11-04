package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorConfigProvider;
import org.rtext.lang.backend.ConnectorFactory;
import org.rtext.lang.backend.DefaultConnectorProvider;
import org.rtext.lang.specs.util.MockInjector;

@CreateWith(MockInjector.class)
@Named("DefaultConnectorProvider")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class DefaultConnectorProviderSpec {
  public DefaultConnectorProvider subject;
  
  @Mock
  ConnectorConfigProvider configFileProvider;
  
  ConnectorConfig config = new ConnectorConfig(new File("path"), "");
  
  @Mock
  Connector connector;
  
  @Mock
  ConnectorFactory connectorFactory;
  
  String aModelPath = "a path";
  
  String anotherModelPath = "another path";
  
  @Before
  public void before() throws Exception {
    DefaultConnectorProvider _defaultConnectorProvider = new DefaultConnectorProvider(this.connectorFactory);
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
  @Named("Creates connector for configuration")
  @Order(1)
  public void _createsConnectorForConfiguration() throws Exception {
    Connector _get = this.subject.get(this.config);
    boolean _doubleArrow = Should.operator_doubleArrow(_get, Connector.class);
    Assert.assertTrue("\nExpected subject.get(config) => typeof(Connector) but"
     + "\n     subject.get(config) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     subject is " + new org.hamcrest.StringDescription().appendValue(this.subject).toString()
     + "\n     config is " + new org.hamcrest.StringDescription().appendValue(this.config).toString() + "\n", _doubleArrow);
    
    ConnectorFactory _verify = Mockito.<ConnectorFactory>verify(this.connectorFactory);
    _verify.createConnector(this.config);
  }
  
  @Test
  @Named("disposes all connectors")
  @Order(2)
  public void _disposesAllConnectors() throws Exception {
    this.subject.get(this.config);
    this.subject.dispose();
    Connector _verify = Mockito.<Connector>verify(this.connector);
    _verify.disconnect();
  }
}
