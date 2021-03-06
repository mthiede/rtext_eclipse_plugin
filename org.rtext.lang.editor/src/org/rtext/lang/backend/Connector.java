/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.util.HashSet;
import java.util.Set;
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
import org.rtext.lang.backend.ConnectorListener;

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
			isBusy = false;
			delegate.handleResponse(response);
		}

		public void handleError(String error) {
			disconnect();
			delegate.handleError(error);
		}
	}
	
	private static final String ADDRESS = "127.0.0.1";
	
	private BackendStarter processRunner;
	private Connection connection;
	private volatile boolean isBusy = false;

	private Callback<LoadedModel> loadModelCallBack;

	private Set<ConnectorListener> listeners = new HashSet<ConnectorListener>();
	
	public static Connector create(ConnectorConfig connectorConfig){
		BackendStarter backendStarter = CliBackendStarter.create(connectorConfig);
		LoadModelCallback modelCallBack = LoadModelCallback.create(connectorConfig);
		TcpClient tcpClient = TcpClient.create();
		return new Connector(backendStarter, tcpClient, modelCallBack);
	}
	
	public Connector(BackendStarter processRunner, Connection connection, Callback<LoadedModel> loadModelCallBack) {
		this.processRunner = processRunner;
		this.connection = connection;
		this.loadModelCallBack = loadModelCallBack;
	}

	public<T extends Response> T execute(Command<T> command) throws TimeoutException, BackendException{
		SynchronousCallBack<T> callback = new SynchronousCallBack<T>();
		execute(command, callback);
		callback.waitForResponse();
		return callback.response();
	}
	
	public <T extends Response> void execute(Command<T> command, Callback<T> callback) throws BackendException{
		if(!connect(requiresModelLoad(command))){
			callback.handleError("Could not connect to backend");
			return;
		}
		sendRequest(command, callback);
		
	}

	public <T extends Response> boolean requiresModelLoad(Command<T> command) {
		return !(command instanceof LoadModelCommand);
	}

	private <T extends Response> void sendRequest(Command<T> command, Callback<T> callback) {
		try{
			isBusy = true;
			connection.sendRequest(command, wrap(callback));
			notifyListenersCommand(command.toString());
		}catch(Throwable e){
			disconnect();
			Exceptions.rethrow(e);
		}
	}
	
	private <T extends Response> Callback<T> wrap(Callback<T> delegate){
		return new OnErrorClosingCallback<T>(delegate);
	}

	private boolean startBackend()  {
		try{
			processRunner.startProcess();
			int port = processRunner.getPort();
			connection.connect(ADDRESS, port);
			notifyListenersAboutConnect(ADDRESS, port);
			return true;
		}catch(Throwable e){
			disconnect();
			return false;
		}
	}

	public void connect() {
		connect(true);
	}
	
	private synchronized boolean connect(boolean loadModel) {
		if(isConnected()){
			return true;
		}
		if(!startBackend()){
			return false;
		}
		if(loadModel){
			sendRequest(new LoadModelCommand(), loadModelCallBack);
		}
		return true;
	}

	public void disconnect(){
		processRunner.stop();
		connection.close();
		notifyListenersAboutDisconnect();
	}

	public boolean isConnected(){
		return processRunner.isRunning();
	}

	public boolean isBusy(){
		return isBusy;
	}
	
	private void notifyListenersAboutConnect(String address, int port) {
		for (ConnectorListener listener : listeners) {
			listener.connect(address, port);
		}
	}
	
	private void notifyListenersCommand(String command) {
		for (ConnectorListener listener : listeners) {
			listener.executeCommand(command);
		}
	}
	
	private void notifyListenersAboutDisconnect() {
		for (ConnectorListener listener : listeners) {
			listener.disconnect();
		}
	}

	public void addListener(ConnectorListener listener){
		listeners.add(listener);
	}

	public void removeListener(ConnectorListener listener){
		listeners.remove(listener);
	}
}
