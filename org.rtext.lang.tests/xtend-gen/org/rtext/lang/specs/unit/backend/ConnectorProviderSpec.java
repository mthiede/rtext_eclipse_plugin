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
import org.rtext.lang.backend2.CachingConnectorProvider;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ConnectorConfigProvider;
import org.rtext.lang.backend2.ConnectorFactory;
import org.rtext.lang.backend2.ConnectorProvider;
import org.rtext.lang.specs.util.MockInjector;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("ConnectorProvider")
@CreateWith(value = MockInjector.class)
public class ConnectorProviderSpec {
  public ConnectorProvider subject;
  
  @Mock
  ConnectorConfigProvider configFileProvider;
  
  @Mock
  ConnectorConfig config;
  
  @Mock
  ConnectorConfig anotherConfig;
  
  @Mock
  Connector connector;
  
  String aModelPath = "a path";
  
  String anotherModelPath = "another path";
  
  @Before
  public void before() throws Exception {
    ConnectorFactory _connectorFactory = new ConnectorFactory();
    CachingConnectorProvider _cachingConnectorProvider = new CachingConnectorProvider(this.configFileProvider, _connectorFactory);
    this.subject = _cachingConnectorProvider;
  }
  
  @Test
  @Named("Creates connector with file specific configuration")
  @Order(0)
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
  @Order(1)
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
  @Order(2)
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
}
