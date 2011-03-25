package org.rtext.backend;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
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
	private File descFile;
	private int state;
	private IOConsole console;
	private IOConsoleOutputStream consoleOutputStream;
	private long invocationId;

	private BufferedInputStream inputStream;
	private BufferedInputStream errorStream;
	private String port;

	private Map<String, Invocation> invocations = new HashMap<String, Invocation>();
	
	private Pattern portPattern = Pattern.compile("listening on port (\\d+)");
		
	private static int OFF = 0;
	private static int STARTUP = 1;
	private static int CONNECTED = 2;
	
	private class Invocation {
		String id;
		IResponseListener listener;
		long expireTime;
		Invocation (String id, IResponseListener listener, int timeout) {
			this.id = id;
			this.listener = listener;
			this.expireTime = new Date().getTime() + timeout;
		}
	}
	
	Connector(File descFile) {
		this.descFile = descFile;
		console = new IOConsole("RText ["+descFile+"]", null);
		consoleOutputStream = console.newOutputStream();
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		manager.addConsoles(new IConsole[] { console });
		state = OFF;
		invocationId = 0;
	}
	
	public StringTokenizer executeCommand(Command command, int timeout) {
		String invId = String.valueOf(invocationId++);
		sendRequest(command.getName()+"\n"+invId+"\n"+command.getData());
		String response = receiveResponse(timeout);
		if (response != null) {
			StringTokenizer st = new StringTokenizer(response, "\n");
			if (st.hasMoreTokens() && st.nextToken().equals(invId)) {
				return st;
			}
		}
		return null;
	}
	
	public void executeCommand(Command command, IResponseListener listener, int timeout) {
		String invId = String.valueOf(invocationId++);
		sendRequest(command.getName()+"\n"+invId+"\n"+command.getData());
		String key = String.valueOf(invId);
		invocations.put(key, new Invocation(key, listener, timeout));
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
				if (response != null) {
					StringTokenizer st = new StringTokenizer(response, "\n");
					if (st.hasMoreTokens()) {
						String key = st.nextToken();
						Invocation inv = invocations.get(key);
						if (inv != null) {
							inv.listener.responseReceived(st);
							invocations.remove(key);
						}
					}
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
	
	void killProcess() {
		if (isProcessRunning()) {
			process.destroy();
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
			byte[] data = new byte[65000];
			DatagramPacket packet = new DatagramPacket(data, 65000);
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
				inv.listener.requestTimedOut();
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
		try {
			FileInputStream fis = new FileInputStream(descFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String cmd = br.readLine();
			fis.close();
			if (cmd != null) {
				process = Runtime.getRuntime().exec(cmd, null, descFile.getParentFile());
				inputStream = new BufferedInputStream(process.getInputStream());
				errorStream = new BufferedInputStream(process.getErrorStream());
			}
		} catch (FileNotFoundException e) {
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
			return true;
		} catch (SocketException e) {
			return false;
		}		
	}

}
