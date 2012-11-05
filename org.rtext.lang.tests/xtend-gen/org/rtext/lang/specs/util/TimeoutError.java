package org.rtext.lang.specs.util;

@SuppressWarnings("all")
public class TimeoutError extends AssertionError {
  public TimeoutError(final String message) {
    super(message);
  }
}
