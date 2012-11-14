package org.rtext.lang.backend2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.rtext.lang.util.Expectations;

public class TcpClient implements Connection {

	private static final int SOCKET_TIMEOUT = 200;

	private class Worker extends Thread {
		private boolean running = true;

		@Override
		public void run() {
			while (running) {
				try {
					Task<?> task = tasks.take();
					executeCommand(task);
					try {
						receiveResponse(task);
					} catch (Exception e) {
						task.callback.handleError(e.getMessage());
						TcpClient.this.close();
						return;
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		protected <T extends Response> void receiveResponse(Task<T> task) throws IOException {
			boolean responseReceived = false;
			while(!responseReceived){
				int messageLength = readMessageLength();
				String message = readMessage(messageLength);
				responseReceived = responseParser.parse(message, task.callback, task.command.getResponseType());
			}
		}

		protected void executeCommand(Task<?> task) {
			out.println(serializer.serialize(task.command));
		}
	}

	public static class Task<T extends Response> {
		private final Command<T> command;
		private final Callback<T> callback;

		public Task(Command<T> command, Callback<T> callback) {
			this.command = command;
			this.callback = callback;
		}
	}
	
	public static TcpClient create() {
		return new TcpClient(new CommandSerializer(), new ResponseParser());
	}

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private CommandSerializer serializer;

	private ResponseParser responseParser;
	private BlockingQueue<Task<?>> tasks = new LinkedBlockingQueue<Task<?>>();
	private Worker worker;

	public TcpClient(CommandSerializer serializer, ResponseParser responseParser) {
		this.serializer = serializer;
		this.responseParser = responseParser;
	}

	public void connect(String address, int port) {
		try {
			Expectations.greaterThanZero(port);
			createSocket(address, port);
			startCommandExecution();
		} catch (UnknownHostException e) {
			throw new BackendException("Unknown host " + address + ":" + port, e);
		} catch (IOException e) {
			throw new BackendException("Exception when connecting to " + address + ":" + port, e);
		}
	}

	protected void startCommandExecution() {
		worker = new Worker();
		worker.start();
	}

	protected void createSocket(String address, int port)
			throws UnknownHostException, IOException {
		socket = new Socket();
		socket.setSoTimeout(SOCKET_TIMEOUT);
		socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void close() {
		if (socket != null) {
			try {
				socket.close();
				out.close();
				in.close();
				worker.running = false;
				socket = null;
			} catch (IOException e) {
				// ignore
			} 
		}
	}

	public <T extends Response> void sendRequest(Command<T> request, Callback<T> callback) {
		if(!isConnected()){
			throw new BackendException("Not connected"); 
		}
		tasks.add(new Task<T>(request, callback));
	}

	protected String readMessage(int messageLength) throws IOException {
		StringBuilder message = new StringBuilder("{");
		int c = in.read();
		for (int i = 0; i < messageLength - 2 && c != -1; i++) {
			message.append((char) c);
			c = in.read();
		}
		message.append((char) c);
		return message.toString();
	}

	private int readMessageLength() throws IOException {
		StringBuilder result = new StringBuilder();
		int c = in.read();
		while (c != -1 && c != '{') {
			result.append((char) c);
			c = in.read();
		}
		int length = Integer.parseInt(result.toString().trim());
		return length;
	}

	public boolean isConnected(){
		return socket != null;
	}

}
