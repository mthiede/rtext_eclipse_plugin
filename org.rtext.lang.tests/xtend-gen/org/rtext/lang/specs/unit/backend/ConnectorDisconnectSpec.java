package org.rtext.lang.specs.unit.backend;

import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rtext.lang.backend.BackendStarter;
import org.rtext.lang.backend.Connection;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("disconnect")
public class ConnectorDisconnectSpec extends ConnectorSpec {
  @Test
  @Named("closes connection")
  @Order(18)
  public void _closesConnection() throws Exception {
    this.subject.disconnect();
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.close();
  }
  
  @Test
  @Named("stops process runner")
  @Order(19)
  public void _stopsProcessRunner() throws Exception {
    this.subject.disconnect();
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.stop();
  }
}
