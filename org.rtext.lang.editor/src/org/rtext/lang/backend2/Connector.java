package org.rtext.lang.backend2;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.editor.LoadModelCallback;

public class Connector {
	
	private static final String ADDRESS = "127.0.0.1";
	
	private final ConnectorConfig connectorConfig;
	private BackendStarter processRunner;
	private Connection connection;

	private Callback<LoadedModel> loadModelCallBack;
	
	public static Connector create(ConnectorConfig connectorConfig){
		return new Connector(connectorConfig, CliBackendStarter.create(), TcpClient.create(), LoadModelCallback.create());
	}
	
	public Connector(ConnectorConfig connectorConfig, BackendStarter processRunner, Connection connection, Callback<LoadedModel> loadModelCallBack) {
		this.connectorConfig = connectorConfig;
		this.processRunner = processRunner;
		this.connection = connection;
		this.loadModelCallBack = loadModelCallBack;
	}

	public<T extends Response> T execute(Command<T> command) throws TimeoutException{
		SynchronousCallBack<T> callback = new SynchronousCallBack<T>();
		execute(command, callback);
		callback.waitForResponse();
		return callback.response();
	}
	
	public <T extends Response> void execute(Command<T> command, Callback<T> callback) throws TimeoutException{
		if(!ensureBackendIsConnected(callback)){
			return;
		}
		connection.sendRequest(command, callback);
	}

	protected boolean ensureBackendIsConnected(Callback<?> callback) throws TimeoutException {
		if(processRunner.isRunning()){
			return true;
		}
		return startBackend(callback);
	}

	public boolean startBackend(Callback<?> callback) throws TimeoutException {
		try{
			processRunner.startProcess(connectorConfig);
			connection.connect(ADDRESS, processRunner.getPort());
			connection.sendRequest(new LoadModelCommand(), loadModelCallBack);
			return true;
		}catch(Exception e){
			processRunner.stop();
			callback.handleError("Could not connect to backend");
			return false;
		}
	}

	public void dispose(){
		processRunner.stop();
		connection.close();
	}
}
