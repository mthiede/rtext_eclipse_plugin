/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.arraycopy;
import static org.rtext.lang.util.Closables.closeQuietly;
import static org.rtext.lang.util.Strings.splitCommand;
import static org.rtext.lang.util.Wait.waitUntil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import org.rtext.lang.editor.RTextConsole;
import org.rtext.lang.util.Condition;
import org.rtext.lang.util.Exceptions;

public final class CliBackendStarter implements BackendStarter {
	
	private final class ShutdownHook extends Thread {
		public void run() {
			if(process != null){
				process.destroy();
			}
		}
	}

	private final class OutputMonitor extends Thread {
		private InputStream inputStream;
		private OutputHandler[] listeners;
		private DataInputStream in;
		private BufferedReader out;

		public OutputMonitor(InputStream inputStream, OutputHandler... listeners) {
			this.inputStream = inputStream;
			this.listeners = listeners;
		}

		@Override
		public void run() {
			in = new DataInputStream(inputStream);
			out = new BufferedReader(new InputStreamReader(in));
			try {
				String strLine;
				while ((strLine = out.readLine()) != null) {
					notifyListeners(strLine);
				}
			} catch (IOException e) {
				CliBackendStarter.this.stop();
			}
		}

		public void notifyListeners(String strLine) {
			for (OutputHandler listener : listeners) {
				listener.handle(strLine);
			}
		}
		
		public void close(){
			closeQuietly(in);
			closeQuietly(out);
		}
	}
	
	private Process process;
	private OutputMonitor outputMonitor;
	private InputStream inputStream;
	private final PortParser portParser;
	private final OutputHandler[] outputHandlers;
	private ShutdownHook shutdownHook;
	private final ConnectorConfig connectorConfig;
	
	public CliBackendStarter(PortParser portParser, ConnectorConfig connectorConfig, OutputHandler... outputHandlers) {
		this.portParser = portParser;
		this.outputHandlers = outputHandlers;
		this.connectorConfig = connectorConfig;
	}

	public void startProcess() throws TimeoutException {
		try {
			startRTextProcess(connectorConfig);
			handleOutputStream();
			registerShutDownHook();
		} catch (Throwable e) {
			e.printStackTrace();
			Exceptions.rethrow(e);
		}
	}

	protected void handleOutputStream() {
		inputStream = new BufferedInputStream(process.getInputStream());
		outputMonitor = new OutputMonitor(inputStream, listeners());
		outputMonitor.start();
	}

	private OutputHandler[] listeners() {
		int length = outputHandlers.length + 1;
		OutputHandler[] listeners = new OutputHandler[length];
		listeners[0] = portParser;
		arraycopy(outputHandlers, 0, listeners, 1, outputHandlers.length);
		return listeners;
	}
	

	protected void startRTextProcess(ConnectorConfig connectorConfig) throws IOException {
		if(isRunning()){
			stop();
		}
		String[] commands = getCommands(connectorConfig);
		process = new ProcessBuilder(commands)
			.redirectErrorStream(true)
			.directory(connectorConfig.getWorkingDir())
			.start();
	}

	public String[] getCommands(ConnectorConfig connectorConfig) {
		String command = connectorConfig.getCommand();
		String[] commands = splitCommand(command);
		return commands;
	}

	private void registerShutDownHook() {
		shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}

	public boolean isRunning() {
		if (process == null) {
			return false;
		}
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	public void stop() {
		if(shutdownHook != null){
			getRuntime().removeShutdownHook(shutdownHook);
		}
		if (process != null) {
			process.destroy();
			closeQuietly(process.getErrorStream());
			closeQuietly(process.getInputStream());
			process = null;
		}
		if(outputMonitor != null){
			outputMonitor.close();
			outputMonitor.interrupt();
		}
		portParser.clear();
	}

	public int getPort() throws TimeoutException {
		waitUntil(new Condition() {
			public boolean applies() {
				return portParser.hasReceivedPort();
			}
		});
		return portParser.getPort();
	}

	public static BackendStarter create(ConnectorConfig connectorConfig) {
		return new CliBackendStarter(new PortParser(), connectorConfig, new RTextConsole(connectorConfig));
	}
}