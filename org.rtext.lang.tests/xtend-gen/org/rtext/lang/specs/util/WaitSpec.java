package org.rtext.lang.specs.util;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.hamcrest.StringDescription;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.rtext.lang.specs.util.Clock;
import org.rtext.lang.specs.util.MockInjector;
import org.rtext.lang.specs.util.Sleeper;
import org.rtext.lang.specs.util.TimeoutError;
import org.rtext.lang.specs.util.Wait;
import org.rtext.lang.specs.util.WaitConfig;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Wait")
@CreateWith(value = MockInjector.class)
public class WaitSpec {
  public Wait subject;
  
  @Extension
  public WaitConfig c = new Function0<WaitConfig>() {
    public WaitConfig apply() {
      WaitConfig _waitConfig = new WaitConfig();
      return _waitConfig;
    }
  }.apply();
  
  @Mock
  Sleeper sleeper;
  
  @Mock
  Clock clock;
  
  @Mock
  Function0<Boolean> condition;
  
  @Before
  public void before() throws Exception {
    Wait _wait = new Wait(this.sleeper, this.clock);
    this.subject = _wait;
  }
  
  @Test
  @Named("waits until condition is true")
  @Order(1)
  public void _waitsUntilConditionIsTrue() throws Exception {
    Boolean _apply = this.condition.apply();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(_apply);
    _when.thenReturn(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true));
    this.waitFor(this.condition);
    VerificationMode _times = Mockito.times(3);
    Function0<Boolean> _verify = Mockito.<Function0<Boolean>>verify(this.condition, _times);
    _verify.apply();
  }
  
  @Test
  @Named("tries every specified polling frequency")
  @Order(2)
  public void _triesEverySpecifiedPollingFrequency() throws Exception {
    this.c.setPollEvery(10l);
    Boolean _apply = this.condition.apply();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(_apply);
    _when.thenReturn(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true));
    this.waitFor(this.condition);
    VerificationMode _times = Mockito.times(2);
    Sleeper _verify = Mockito.<Sleeper>verify(this.sleeper, _times);
    long _pollEvery = this.c.getPollEvery();
    _verify.sleep(_pollEvery);
  }
  
  @Test
  @Named("throws TimeoutError after specified time out")
  @Order(3)
  public void _throwsTimeoutErrorAfterSpecifiedTimeOut() throws Exception {
    this.c.setDuration(100l);
    Boolean _apply = this.condition.apply();
    OngoingStubbing<Boolean> _when = Mockito.<Boolean>when(_apply);
    _when.thenReturn(Boolean.valueOf(false));
    long _currentTime = this.clock.currentTime();
    OngoingStubbing<Long> _when_1 = Mockito.<Long>when(Long.valueOf(_currentTime));
    _when_1.thenReturn(Long.valueOf(0l), Long.valueOf(50l), Long.valueOf(100l), Long.valueOf(150l));
    try{
      this.waitFor(this.condition);
      Assert.fail("Expected " + TimeoutError.class.getName() + " in \n     waitFor(condition)\n with:"
       + "\n     condition is " + new StringDescription().appendValue(this.condition).toString());
    }catch(TimeoutError e){
    }
  }
  
  public void waitFor(final Function0<Boolean> condition) {
    this.subject.until(condition, this.c);
  }
}
