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
@Named("Dispose")
public class ConnectorDisposeSpec extends ConnectorSpec {
  @Test
  @Named("Disposes connection")
  @Order(16)
  public void _disposesConnection() throws Exception {
    this.subject.dispose();
    Connection _verify = Mockito.<Connection>verify(this.connection);
    _verify.close();
  }
  
  @Test
  @Named("Stops process runner")
  @Order(17)
  public void _stopsProcessRunner() throws Exception {
    this.subject.dispose();
    BackendStarter _verify = Mockito.<BackendStarter>verify(this.processRunner);
    _verify.stop();
  }
}
