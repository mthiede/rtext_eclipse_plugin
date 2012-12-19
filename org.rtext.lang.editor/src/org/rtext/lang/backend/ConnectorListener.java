package org.rtext.lang.backend;


public interface ConnectorListener {
	public void executeCommand(String command);
	public void connect(String address, int port);
	public void disconnect();
}
