/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.rtext.lang.RTextPlugin;

public class WorkspaceCallback<T extends Response> implements Callback<T> {

	public class CommandCallbackJob extends Job{

		private IProgressMonitor monitor = new NullProgressMonitor();
		int lastProgress = 0;

		public CommandCallbackJob() {
			super(message);
		}
		
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			this.monitor = monitor;
			monitor.beginTask(message, 100);
			return Job.ASYNC_FINISH;
		}

		public void handleProgress(Progress progress) {
		}

		public void handleError(String error) {
			monitor.done();
			done(new Status(IStatus.ERROR, RTextPlugin.PLUGIN_ID, error));
		}

		public void responseReceived() {
			monitor.done();
			done(Status.OK_STATUS);
		}
	}

	private final String message;
	private CommandCallbackJob callbackJob;
	
	public WorkspaceCallback(String message) {
		this.message = message;
	}
	
	public void handleProgress(Progress progress) {
		callbackJob.handleProgress(progress);
	}

	public void handleResponse(T response) {
		callbackJob.responseReceived();
	}

	public void handleError(String error) {
		callbackJob.handleError(error);
	}

	public void commandSent() {
		if(callbackJob != null){
			callbackJob.cancel();
		}
		callbackJob = new CommandCallbackJob();
		callbackJob.schedule();
	}
}
