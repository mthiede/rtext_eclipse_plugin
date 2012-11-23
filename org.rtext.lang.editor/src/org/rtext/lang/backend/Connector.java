/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.util.concurrent.TimeoutException;

import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.LoadModelCallback;
import org.rtext.lang.commands.LoadModelCommand;
import org.rtext.lang.commands.LoadedModel;
import org.rtext.lang.commands.Progress;
import org.rtext.lang.commands.Response;
import org.rtext.lang.commands.SynchronousCallBack;
import org.rtext.lang.util.Exceptions;

public class Connector {
	
	private class OnErrorClosingCallback<T extends Response> implements Callback<T>{

		private Callback<T> delegate;

		public OnErrorClosingCallback(Callback<T> delegate) {
			this.delegate = delegate;
		}

		public void commandSent() {
			delegate.commandSent();
		}

		public void handleProgress(Progress progress) {
			delegate.handleProgress(progress);
		}

		public void handleResponse(T response) {
			delegate.handleResponse(response);
		}

		public void handleError(String error) {
			dispose();
			delegate.handleError(error);
		}
		
	}
	
	private static final String ADDRESS = "127.0.0.1";
	
	private final ConnectorConfig connectorConfig;
	private BackendStarter processRunner;
	private Connection connection;

	private Callback<LoadedModel> loadModelCallBack;
	
	public static Connector create(ConnectorConfig connectorConfig){
		BackendStarter backendStarter = CliBackendStarter.create(connectorConfig);
		LoadModelCallback modelCallBack = LoadModelCallback.create();
		TcpClient tcpClient = TcpClient.create();
		return new Connector(connectorConfig, backendStarter, tcpClient, modelCallBack);
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
	
	public <T extends Response> void execute(Command<T> command, Callback<T> callback){
		if(!ensureBackendIsConnected(callback)){
			return;
		}
		try{
			sendRequest(command, callback);
		}catch(Throwable e){
			dispose();
			Exceptions.rethrow(e);
		}
	}

	public <T extends Response> void sendRequest(Command<T> command,
			Callback<T> callback) {
		connection.sendRequest(command, wrap(callback));
	}
	
	public <T extends Response> Callback<T> wrap(Callback<T> delegate){
		return new OnErrorClosingCallback<T>(delegate);
	}

	protected boolean ensureBackendIsConnected(Callback<?> callback) {
		if(processRunner.isRunning()){
			return true;
		}
		return startBackend(callback);
	}

	public boolean startBackend(Callback<?> callback)  {
		try{
			processRunner.startProcess(connectorConfig);
			connection.connect(ADDRESS, processRunner.getPort());
			sendRequest(new LoadModelCommand(), loadModelCallBack);
			return true;
		}catch(Throwable e){
			dispose();
			callback.handleError("Could not connect to backend");
			return false;
		}
	}

	public void dispose(){
		processRunner.stop();
		connection.close();
	}
	
}
