package org.rtext.lang.backend2;

import static java.lang.System.arraycopy;
import static org.rtext.lang.util.Strings.splitCommand;
import static org.rtext.lang.util.Wait.waitUntil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.util.Condition;
import org.rtext.lang.util.Exceptions;
import org.rtext.lang.util.Strings;

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

		public OutputMonitor(InputStream inputStream, OutputHandler... listeners) {
			this.inputStream = inputStream;
			this.listeners = listeners;
		}

		@Override
		public void run() {
			DataInputStream in = new DataInputStream(inputStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			try {
				String strLine;
				while ((strLine = br.readLine()) != null) {
					for (OutputHandler listener : listeners) {
						listener.handle(strLine);
					}
				}
			} catch (IOException e) {
				CliBackendStarter.this.stop();
			}
		}
	}
	
	private Process process;
	private OutputMonitor outputMonitor;
	private InputStream inputStream;
	private PortParser portParser;
	private OutputHandler[] outputHandlers;
	private ShutdownHook shutdownHook;
	
	public CliBackendStarter(PortParser portParser, OutputHandler... outputHandlers) {
		this.portParser = portParser;
		this.outputHandlers = outputHandlers;
	}

	public void startProcess(ConnectorConfig connectorConfig) throws TimeoutException {
		try {
			startRTextProcess(connectorConfig);
			handleOutputStream();
			registerShutDownHook();
		} catch (IOException e) {
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

	protected void startRTextProcess(ConnectorConfig connectorConfig)
			throws IOException {
		String command = connectorConfig.getCommand();
		String[] commands = splitCommand(command);
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		process = processBuilder.redirectErrorStream(true)
			.directory(connectorConfig.getWorkingDir())
			.start();
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
		Runtime.getRuntime().removeShutdownHook(shutdownHook);
		if (process != null) {
			process.destroy();
		}
	}

	public int getPort() throws TimeoutException {
		waitUntil(new Condition() {
			public boolean applies() {
				return portParser.hasReceivedPort();
			}
		});
		return portParser.getPort();
	}

	public static BackendStarter create() {
		return new CliBackendStarter(new PortParser(), new SystemOutDebug());
	}
}