package org.rtext.lang.backend;

import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.workspace.BackendConnectJob;
import org.rtext.lang.workspace.BackendConnectJob.Factory;

public class CommandExecutor {

	public static final String MODEL_NOT_YET_LOADED = "model not yet loaded";
	public static final String BACKEND_NOT_YET_AVAILABLE = "Backend not yet available";
	public static final String LOADING_MODEL = "loading model";
	public static final String BACKEND_NOT_FOUND = "Backend not found.";

	public interface ExecutionHandler<T> {
		void handleResult(T response);
		void handle(String message);
	}

	public static CommandExecutor create(Connected connected){
		return new CommandExecutor(connected, new BackendConnectJob.Factory());
	}

	public CommandExecutor(Connected connected, Factory connectJobFactory) {
		this.connected = connected;
		this.connectJobFactory = connectJobFactory;
	}
	
	private Factory connectJobFactory;
	private BackendConnectJob backendConnectJob;
	private Connected connected;

	public <T extends Response> void run(Command<T> command, ExecutionHandler<T> executionHandler) {
		Connector connector = connected.getConnector();
		if (connector == null) {
			executionHandler.handle(BACKEND_NOT_FOUND);
			return;
		}
		if (!connector.isConnected()) {
			tryToConnect(executionHandler, connector);
			return;
		}
		if (connector.isBusy()) {
			executionHandler.handle(LOADING_MODEL);
			return;
		}
		try {
			executionHandler.handleResult(connector.execute(command));
		} catch (Exception e) {
			executionHandler.handle(BACKEND_NOT_YET_AVAILABLE);
		}
	}

	private void tryToConnect(ExecutionHandler<?> executionHandler,
			Connector connector) {
		if (backendConnectJob == null || !backendConnectJob.isRunning()) {
			backendConnectJob = connectJobFactory.create(connector);
			backendConnectJob.execute();
			executionHandler.handle(MODEL_NOT_YET_LOADED);
		} else {
			executionHandler.handle(BACKEND_NOT_YET_AVAILABLE);
		}
	}
}
