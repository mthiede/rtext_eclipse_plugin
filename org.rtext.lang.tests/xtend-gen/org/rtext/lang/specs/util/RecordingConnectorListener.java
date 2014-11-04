package org.rtext.lang.specs.util;

import java.util.ArrayList;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;
import org.rtext.lang.backend.ConnectorListener;

@Data
@SuppressWarnings("all")
public class RecordingConnectorListener implements ConnectorListener {
  private final ArrayList<String> _log = CollectionLiterals.<String>newArrayList();
  
  public void connect(final String address, final int port) {
    ArrayList<String> _log = this.getLog();
    _log.add(((("connect " + address) + ":") + Integer.valueOf(port)));
  }
  
  public void disconnect() {
    ArrayList<String> _log = this.getLog();
    _log.add("disconnect");
  }
  
  public void executeCommand(final String command) {
    ArrayList<String> _log = this.getLog();
    _log.add(command);
  }
  
  public boolean recorded(final String message) {
    ArrayList<String> _log = this.getLog();
    return _log.contains(message);
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._log== null) ? 0 : this._log.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RecordingConnectorListener other = (RecordingConnectorListener) obj;
    if (this._log == null) {
      if (other._log != null)
        return false;
    } else if (!this._log.equals(other._log))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
  
  @Pure
  public ArrayList<String> getLog() {
    return this._log;
  }
}
