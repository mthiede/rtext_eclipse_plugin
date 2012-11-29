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
  public static void waitUntil(final Function1<WaitConfig,Boolean> initializer) {
    WaitConfig _waitConfig = new WaitConfig();
    final WaitConfig config = _waitConfig;
    Wait _wait = new Wait(Sleeper.SYSTEM_SLEEPER, Clock.SYSTEM_CLOCK);
    final Wait wait = _wait;
    final Function0<Boolean> _function = new Function0<Boolean>() {
        public Boolean apply() {
          Boolean _apply = initializer.apply(config);
          return _apply;
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
      Boolean _apply = condition.apply();
      boolean _not = (!_apply);
      boolean _while = _not;
      while (_while) {
        {
          boolean _timeOut = this.timeOut(config, start);
          if (_timeOut) {
            String _message = config.getMessage();
            TimeoutError _timeoutError = new TimeoutError(_message);
            throw _timeoutError;
          }
          long _pollEvery = config.getPollEvery();
          this.sleeper.sleep(_pollEvery);
        }
        Boolean _apply_1 = condition.apply();
        boolean _not_1 = (!_apply_1);
        _while = _not_1;
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public boolean timeOut(final WaitConfig config, final long start) {
    long _currentTime = this.clock.currentTime();
    long _duration = config.getDuration();
    long _plus = (start + _duration);
    boolean _greaterThan = (_currentTime > _plus);
    return _greaterThan;
  }
}
