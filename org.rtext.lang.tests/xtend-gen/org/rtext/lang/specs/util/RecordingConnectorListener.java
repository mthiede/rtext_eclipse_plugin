package org.rtext.lang.specs.util;

import java.util.ArrayList;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;
import org.rtext.lang.backend.ConnectorListener;

@Data
@SuppressWarnings("all")
public class RecordingConnectorListener implements ConnectorListener {
  private final ArrayList<String> _log = new Function0<ArrayList<String>>() {
    public ArrayList<String> apply() {
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public ArrayList<String> getLog() {
    return this._log;
  }
  
  public void connect(final String address, final int port) {
    ArrayList<String> _log = this.getLog();
    String _plus = ("connect " + address);
    String _plus_1 = (_plus + ":");
    String _plus_2 = (_plus_1 + Integer.valueOf(port));
    _log.add(_plus_2);
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
    boolean _contains = _log.contains(message);
    return _contains;
  }
  
  public RecordingConnectorListener() {
    super();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_log== null) ? 0 : _log.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RecordingConnectorListener other = (RecordingConnectorListener) obj;
    if (_log == null) {
      if (other._log != null)
        return false;
    } else if (!_log.equals(other._log))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
