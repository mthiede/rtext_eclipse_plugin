package org.rtext.lang.backend;

public class ConnectorLogger extends AbstractLogger implements
		ConnectorListener {

	public void executeCommand(String command) {
		info(command);
	}

	public void connect(String address, int port) {
		info("connect to " + address + ":" + port);
	}

	public void disconnect() {
		info("disconnect");
	}

}
