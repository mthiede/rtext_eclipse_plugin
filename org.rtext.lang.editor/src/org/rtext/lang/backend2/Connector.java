package org.rtext.lang.backend2;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.backend.ConnectorConfig;

public class Connector {
	
	private static final String ADDRESS = "127.0.0.1";
	
	private final ConnectorConfig connectorConfig;
	private BackendStarter processRunner;
	private Connection connection;
	
	public static Connector create(ConnectorConfig connectorConfig){
		return new Connector(connectorConfig, CliBackendStarter.create(), TcpClient.create());
	}
	
	public Connector(ConnectorConfig connectorConfig, BackendStarter processRunner, Connection connection) {
		this.connectorConfig = connectorConfig;
		this.processRunner = processRunner;
		this.connection = connection;
	}

	public<T extends Response> T execute(Command<T> command) throws TimeoutException{
		SynchronousCallBack<T> callback = new SynchronousCallBack<T>();
		execute(command, callback);
		callback.waitForResponse();
		return callback.response();
	}

	protected boolean ensureBackendIsConnected(Callback<?> callback) throws TimeoutException {
		if(!processRunner.isRunning()){
			processRunner.startProcess(connectorConfig);
			try{
				connection.connect(ADDRESS, processRunner.getPort());
			}catch(Exception e){
				processRunner.stop();
				callback.handleError("Could not connect to backend");
				return false;
			}
		}
		return true;
	}

	public <T extends Response> void execute(Command<T> command, Callback<T> callback) throws TimeoutException{
		if(!ensureBackendIsConnected(callback)){
			return;
		}
		connection.sendRequest(command, callback);
	}
	
	public void dispose(){
		processRunner.stop();
		connection.close();
	}
	
}
