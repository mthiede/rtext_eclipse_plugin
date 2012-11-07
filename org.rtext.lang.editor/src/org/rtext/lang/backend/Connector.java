/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;


public class Connector {
	private DatagramSocket socket;
	private Process process;
	private ConnectorConfig config;
	private int state;
	private IOConsole console;
	private IOConsoleOutputStream consoleOutputStream;
	private long invocationId;
	private int protocolVersion = -1;

	private BufferedInputStream inputStream;
	private BufferedInputStream errorStream;
	private String port;

	private Map<String, Invocation> invocations = new HashMap<String, Invocation>();
	
	private Pattern portPattern = Pattern.compile("listening on port (\\d+)");
		
	private static int OFF = 0;
	private static int STARTUP = 1;
	private static int CONNECTED = 2;
	
	public static int ERROR_OK = 0;
	public static int ERROR_NOT_CONNECTED = 1;
	
	private class Invocation {
		String id;
		IResponseListener listener;
		long expireTime;
		ArrayList<String> responseLines;
		Invocation (String id, IResponseListener listener, int timeout) {
			this.id = id;
			this.listener = listener;
			this.expireTime = new Date().getTime() + timeout;
			this.responseLines = new ArrayList<String>();
		}
	}
	
	Connector(ConnectorConfig config) {
		this.config = config;
		console = new IOConsole("RText ["+config.getConfigFile()+"]", null);
		consoleOutputStream = console.newOutputStream();
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		manager.addConsoles(new IConsole[] { console });
		state = OFF;
		invocationId = 0;
	}
	
	public List<String> executeCommand(Command command, int timeout) {
		if (state == CONNECTED) {
			String invId = String.valueOf(invocationId++);
			sendRequest(command.getName()+"\n"+invId+"\n"+command.getData());
			String key = String.valueOf(invId);
			invocations.put(key, new Invocation(key, null, timeout));
			String response = receiveResponse(timeout);
			while (response != null) {
				List<String> responseLines = handleResponse(response);
				if (responseLines != null) {
					return responseLines;
				}
				response = receiveResponse(100);
			}
		}
		return null;
	}
	
	public int executeCommand(Command command, IResponseListener listener, int timeout) {
		if (state == CONNECTED) {
			String invId = String.valueOf(invocationId++);
			sendRequest(command.getName()+"\n"+invId+"\n"+command.getData());
			String key = String.valueOf(invId);
			invocations.put(key, new Invocation(key, listener, timeout));
			return ERROR_OK;
		}
		else {
			return ERROR_NOT_CONNECTED;
		}
	}

	public int getProtocolVersion() {
		if (protocolVersion < 0) {
			List<String> result = executeCommand(new Command("protocol_version", ""), 10000);
			if (result != null && result.size() > 0) {
				protocolVersion = Integer.parseInt(result.get(0));
			}
			else {
				protocolVersion = 0;
			}
		}
		return protocolVersion;
	}
	
	public ConnectorConfig getConfig() {
		return config;
	}
	
	void updateConnector() {
		if (isProcessRunning()) {
			if (state == STARTUP) {
				if (port != null) {
					if (connectSocket(Integer.parseInt(port))) {
						state = CONNECTED;
					}
				}
			}
			else {
				String response = receiveResponse(1);
				while (response != null) {
					handleResponse(response);
					response = receiveResponse(1);
				}
			}
		}
		else {
			port = null;
			startProcess();
			state = STARTUP;
		}
		handleInvocationTimeouts();
	}

	private List<String> handleResponse(String response) {
		StringTokenizer st = new StringTokenizer(response, "\n");
		if (st.hasMoreTokens()) {
			String key = st.nextToken();
			Invocation inv = invocations.get(key);
			if (inv != null) {
				if (st.hasMoreTokens()) {
					String packetType = st.nextToken();
					while (st.hasMoreTokens()) {
						inv.responseLines.add(st.nextToken());
						if (inv.listener != null) {
							inv.listener.responseUpdate(inv.responseLines);
						}
					}
					if (packetType.equals("last")) {
						invocations.remove(key);
						if (inv.listener != null) {
							inv.listener.responseReceived(inv.responseLines);
							return null;
						}
						else {
							return inv.responseLines;
						}
					}
				}
			}
		}
		return null;
	}
	
	void handleProcessOutput() {
		if (console != null && consoleOutputStream != null) {
			if (inputStream != null && errorStream != null) {
				String output = handleStreamOutput(inputStream);
				if (output != null) {
					String port = parsePort(output);
					if (port != null) {
						this.port = port;
					}
				}
				handleStreamOutput(errorStream);
			}
		}
	}
	
	private void sendRequest(String request) {
		if (state == CONNECTED) {
			DatagramPacket datagramPacket = new DatagramPacket(request.getBytes(), request.getBytes().length);
			try {
				socket.send(datagramPacket);
			} catch (IOException e) {
			}
		}
	}
	
	private String receiveResponse(int timeout) {
		if (state == CONNECTED) {
			byte[] data = new byte[65536];
			DatagramPacket packet = new DatagramPacket(data, 65536);
			try {
				socket.setSoTimeout(timeout);
				socket.receive(packet);
				return new String(packet.getData(), 0, packet.getLength());
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	private void handleInvocationTimeouts() {
		long now = new Date().getTime();
		Object[] currentValues = invocations.values().toArray();
		for (int i = 0; i < currentValues.length; i++) {
			Invocation inv = (Invocation)currentValues[i];
			if (now > inv.expireTime) {
				if (inv.listener != null) {
					inv.listener.requestTimedOut();
				}
				invocations.remove(inv.id);
			}
		}
	}
	
	private boolean isProcessRunning() {
		if (process != null) {
			try {
				process.exitValue();
				return false;
			}
			catch (IllegalThreadStateException e) {
				return true;
			}
		}
		else {
			return false;
		}
	}
	
	private void startProcess() {
		protocolVersion = -1;
		try {
			process = Runtime.getRuntime().exec(config.getCommand(), null, config.getConfigFile().getParentFile());
			inputStream = new BufferedInputStream(process.getInputStream());
			errorStream = new BufferedInputStream(process.getErrorStream());
		} catch (IOException e) {
		}		
	}
	
	private String parsePort(String text) {
		Matcher matcher = portPattern.matcher(text);
		if (matcher.find()) {
			return matcher.toMatchResult().group(1).toString();
		}
		return null;
	}
	
	private String handleStreamOutput(InputStream is) {
		if (is != null) {
			try {
				int len = is.available();
				if (len > 0) {
					byte[] data = new byte[len];
					is.read(data, 0, len);
					consoleOutputStream.write(data);
					return new String(data);
				}
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	private boolean connectSocket(int port) {
		try {
			SocketAddress sa = new InetSocketAddress("localhost", port);
			socket = new DatagramSocket();
			socket.connect(sa);
			socket.setReceiveBufferSize(6553600);
			return true;
		} catch (SocketException e) {
			return false;
		}		
	}

}
