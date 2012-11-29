package org.rtext.lang.workspace;

import org.eclipse.core.runtime.jobs.Job;

public abstract class RTextJob extends Job {

	public static final String RTEXT_JOB_FAMILY = "RText Jobs";

	public RTextJob(String name) {
		super(name);
	}

	@Override
	public boolean belongsTo(Object family) {
		if(family == RTextJob.RTEXT_JOB_FAMILY){
			return true;
		}
		return super.belongsTo(family);
	}

}