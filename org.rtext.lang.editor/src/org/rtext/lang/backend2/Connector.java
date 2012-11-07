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

	public Response execute(Command command) throws TimeoutException{
		SynchronousCallBack callback = new SynchronousCallBack();
		execute(command, callback);
		callback.waitForResponse();
		return callback.response();
	}

	protected void ensureBackendIsConnected() throws TimeoutException {
		if(!processRunner.isRunning()){
			processRunner.startProcess(connectorConfig);
			connection.connect(ADDRESS, processRunner.getPort());
		}
	}

	public void execute(Command command, Callback callback) throws TimeoutException{
		ensureBackendIsConnected();
		connection.sendRequest(command, callback);
	}
	
	public void dispose(){
		processRunner.stop();
		connection.close();
	}
	
}
