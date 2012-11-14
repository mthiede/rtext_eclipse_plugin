package org.rtext.lang.backend2;

public interface Connection {
	
	public void connect(String address, int port);
	public <T extends Response> void sendRequest(Command<T> request, Callback<T> callback);
	public void close();

}
