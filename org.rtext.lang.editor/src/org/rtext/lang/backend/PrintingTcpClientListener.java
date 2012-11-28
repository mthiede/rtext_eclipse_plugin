package org.rtext.lang.backend;

public class PrintingTcpClientListener implements TcpClientListener {

	public void connected(String address, int port) {
		info("connected to " + address +":" + port);
	}

	public void messageSent(String jsonMessage) {
		info("request '" + jsonMessage + "'");
	}

	public void messageReceived(String jsonMessage) {
		info("received '" + jsonMessage + "'");
	}
	
	private void info(String message) {
		print("INFO", message);
	}
	
	private void error(Throwable t) {
		print("ERROR", t.getMessage());
		t.printStackTrace();
	}

	private void print(String category, String message) {
		System.out.println("[" + category + "] [Thread " + Thread.currentThread().getId() + "] "  + " TCPClient: " + message);
	}

	public void receiveError(Throwable exception) {
		error(exception);
	}

	public void close() {
		info("shutdown");
	}

}
