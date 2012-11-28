package org.rtext.lang.backend;

public interface TcpClientListener {

	public void connected(String address, int port);

	public void messageSent(String jsonMessage);

	public void messageReceived(String jsonMessage);

	public void receiveError(Throwable exception);

	public void close();

}
