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

public class WorkspaceCallback<T extends Response> extends Job implements Callback<T> {
	
	public static final String RTEXT_JOB_FAMILY = "RText Jobs";
	private IProgressMonitor monitor = new NullProgressMonitor();
	private String message;
	int lastProgress = 0;

	public WorkspaceCallback(String message) {
		super(message);
		this.message = message;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		monitor.beginTask(message, 100);
		return Job.ASYNC_FINISH;
	}
	
	public void handleProgress(Progress progress) {
		int percentage = progress.getPercentage();
		monitor.worked(percentage - lastProgress);
		lastProgress = percentage;
	}

	public void handleResponse(T response) {
		monitor.done();
		done(Status.OK_STATUS);
	}

	public void handleError(String error) {
		monitor.done();
		done(new Status(IStatus.ERROR, RTextPlugin.PLUGIN_ID, error));
	}
	
	public void commandSent() {
		schedule();
	}
	
	@Override
	public boolean belongsTo(Object family) {
		if(family == RTEXT_JOB_FAMILY){
			return true;
		}
		return super.belongsTo(family);
	}
}
