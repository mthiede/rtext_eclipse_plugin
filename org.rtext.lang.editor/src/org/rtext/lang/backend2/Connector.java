/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend2;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Response;
import org.rtext.lang.commands.SynchronousCallBack;

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
		try{
			connection.sendRequest(command, callback);
		}catch(RuntimeException e){
			processRunner.stop();
			throw e;
		}
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
