package org.rtext.lang.specs.unit.backend;

import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.backend.ConnectorListener;
import org.rtext.lang.commands.Response;
import org.rtext.lang.specs.unit.backend.ConnectorSpec;
import org.rtext.lang.specs.util.Commands;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("listener")
public class ConnectorListenerSpec extends ConnectorSpec {
  @Mock
  ConnectorListener listener;
  
  @Before
  public void before2() throws Exception {
    this.subject.addListener(this.listener);
  }
  
  @Test
  @Named("notifies connect")
  @Order(20)
  public void _notifiesConnect() throws Exception {
    this.subject.connect();
    ConnectorListener _verify = Mockito.<ConnectorListener>verify(this.listener);
    _verify.connect(this.ADDRESS, this.PORT);
  }
  
  @Test
  @Named("notifies disconnect")
  @Order(21)
  public void _notifiesDisconnect() throws Exception {
    this.subject.disconnect();
    ConnectorListener _verify = Mockito.<ConnectorListener>verify(this.listener);
    _verify.disconnect();
  }
  
  @Test
  @Named("notifies command send")
  @Order(22)
  public void _notifiesCommandSend() throws Exception {
    this.subject.<Response>execute(Commands.ANY_COMMAND, this.callback);
    ConnectorListener _verify = Mockito.<ConnectorListener>verify(this.listener);
    String _string = Commands.ANY_COMMAND.toString();
    _verify.executeCommand(_string);
  }
  
  @Test
  @Named("removes listeners")
  @Order(23)
  public void _removesListeners() throws Exception {
    this.subject.removeListener(this.listener);
    this.subject.disconnect();
    VerificationMode _never = Mockito.never();
    ConnectorListener _verify = Mockito.<ConnectorListener>verify(this.listener, _never);
    _verify.disconnect();
  }
}
