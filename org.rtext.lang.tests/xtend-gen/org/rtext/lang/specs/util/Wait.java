package org.rtext.lang.specs.util;

import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.rtext.lang.specs.util.Clock;
import org.rtext.lang.specs.util.Sleeper;
import org.rtext.lang.specs.util.TimeoutError;
import org.rtext.lang.specs.util.WaitConfig;

@SuppressWarnings("all")
public class Wait {
  public static void waitUntil(final Function1<WaitConfig, Boolean> initializer) {
    final WaitConfig config = new WaitConfig();
    final Wait wait = new Wait(Sleeper.SYSTEM_SLEEPER, Clock.SYSTEM_CLOCK);
    final Function0<Boolean> _function = new Function0<Boolean>() {
      public Boolean apply() {
        return initializer.apply(config);
      }
    };
    final Function0<Boolean> condition = _function;
    wait.until(condition, config);
  }
  
  private Sleeper sleeper;
  
  private Clock clock;
  
  public Wait(final Sleeper sleeper, final Clock clock) {
    this.sleeper = sleeper;
    this.clock = clock;
  }
  
  public void until(final Function0<Boolean> condition, final WaitConfig config) {
    try {
      final long start = this.clock.currentTime();
      while ((!(condition.apply()).booleanValue())) {
        {
          boolean _timeOut = this.timeOut(config, start);
          if (_timeOut) {
            throw new TimeoutError(config.message);
          }
          this.sleeper.sleep(config.pollEvery);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public boolean timeOut(final WaitConfig config, final long start) {
    long _currentTime = this.clock.currentTime();
    return (_currentTime > (start + config.duration));
  }
}
