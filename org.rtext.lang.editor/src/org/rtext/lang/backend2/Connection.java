package org.rtext.lang.backend2;

public interface Connection {
	
	public void connect(String address, int port);
	public void sendRequest(Command request, Callback callback);
	public void close();

}
