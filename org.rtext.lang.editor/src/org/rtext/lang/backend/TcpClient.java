/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import static java.lang.Integer.parseInt;
import static org.rtext.lang.util.Closables.closeQuietly;
import static org.rtext.lang.util.Expectations.greaterThanZero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.rtext.lang.backend.CommandQueue.Task;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.CommandSerializer;
import org.rtext.lang.commands.Response;
import org.rtext.lang.commands.ResponseParser;

public class TcpClient implements Connection {

	private static final int SOCKET_TIMEOUT = 30000;

	private class Worker extends Thread {
		private volatile boolean running = true;

		@Override
		public void run() {
			while (running) {
				try {
					Task<?> task = commandQueue.take();
					try {
						executeCommand(task);
						receiveResponse(task);
					} catch (Throwable e) {
						handleException(task, e);
						return;
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		public void handleException(Task<?> task, Throwable e) {
			task.callback.handleError(e.getMessage());
			listener.receiveError(e);
			TcpClient.this.close();
		}

		protected <T extends Response> void receiveResponse(Task<T> task)
				throws IOException {
			boolean responseReceived = false;
			while (!responseReceived) {
				int messageLength = readMessageLength();
				String message = readMessage(messageLength);
				responseReceived = responseParser.parse(message, task.callback,	task.command.getResponseType());
				if(responseReceived){
					listener.messageReceived(message);
				}
			}
		}

		protected void executeCommand(Task<?> task) {
			String message = serializer.serialize(task.command);
			out.print(message);
			out.flush();
			task.callback.commandSent();
			listener.messageSent(message);
		}

		public void stopWorking() {
			running = false;
		}
	}

	public static TcpClient create() {
		return create(new PrintingTcpClientListener());
	}
	
	public static TcpClient create(TcpClientListener listener) {
		return new TcpClient(new CommandSerializer(), new ResponseParser(), listener);
	}

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private CommandSerializer serializer;

	private ResponseParser responseParser;
	private CommandQueue commandQueue = new CommandQueue();
	private Worker worker;
	private TcpClientListener listener;
	private String address;
	private int port;

	public TcpClient(CommandSerializer serializer, ResponseParser responseParser, TcpClientListener listener) {
		this.serializer = serializer;
		this.responseParser = responseParser;
		this.listener = listener;
	}

	public synchronized void connect(String address, int port) throws BackendException {
		try {
			if(isAlreadyConnectedTo(address, port)){
				return;
			}
			doConnect(address, port);
		} catch (UnknownHostException e) {
			throw new BackendException("Unknown host " + address + ":" + port,
					e);
		} catch (IOException e) {
			throw new BackendException("Exception when connecting to "
					+ address + ":" + port, e);
		}
	}

	private boolean isAlreadyConnectedTo(String address, int port) {
		return this.address == address && this.port == port && isConnected();
	}

	private void doConnect(String address, int port)
			throws UnknownHostException, IOException {
		close();
		greaterThanZero(port);
		this.address = address;
		this.port = port;
		createSocket(address, port);
		listener.connected(address, port);
		startCommandExecution();
	}

	protected void startCommandExecution() {
		worker = new Worker();
		worker.start();
	}

	protected void createSocket(String address, int port)
			throws UnknownHostException, IOException {
		socket = new Socket();
		socket.setSoTimeout(SOCKET_TIMEOUT);
		socket.setReceiveBufferSize(64000);
		socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void close() {
		stopWorker();
		closeQuietly(socket);
		closeQuietly(out);
		closeQuietly(in);
		socket = null;
		listener.close();
	}

	public void stopWorker() {
		if(worker != null){
			worker.stopWorking();
			commandQueue.clear();
			worker = null;
		}
	}

	public <T extends Response> void sendRequest(Command<T> request,
			Callback<T> callback) throws BackendException {
		if (!isConnected()) {
			throw new BackendException("Not connected");
		}
		commandQueue.add(Task.create(request, callback));
	}

	protected String readMessage(int messageLength) throws IOException {
		StringBuilder message = new StringBuilder("{");
		int c = in.read();
		for (int i = 0; i < messageLength-2 && c != -1; i++) {
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
		int length = parseInt(result.toString().trim());
		return length;
	}

	public boolean isConnected() {
		return socket != null;
	}

}
