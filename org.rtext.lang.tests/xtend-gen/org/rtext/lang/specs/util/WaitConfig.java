package org.rtext.lang.specs.util;

@SuppressWarnings("all")
public class WaitConfig {
  private String _message = "Timeout occurred";
  
  public String getMessage() {
    return this._message;
  }
  
  public void setMessage(final String message) {
    this._message = message;
  }
  
  private long _duration = 5000l;
  
  public long getDuration() {
    return this._duration;
  }
  
  public void setDuration(final long duration) {
    this._duration = duration;
  }
  
  private long _pollEvery = 50l;
  
  public long getPollEvery() {
    return this._pollEvery;
  }
  
  public void setPollEvery(final long pollEvery) {
    this._pollEvery = pollEvery;
  }
}
