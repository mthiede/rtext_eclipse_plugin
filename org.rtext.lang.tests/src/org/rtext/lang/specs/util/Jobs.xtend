package org.rtext.lang.specs.util

import org.eclipse.core.runtime.jobs.Job
import org.rtext.lang.commands.LoadModelCallback

class Jobs {
	
	def static waitForRTextJobs(){
		val jobMan = Job::getJobManager();
		val build = jobMan.find(LoadModelCallback::RTEXT_JOB_FAMILY); 
		if(build.empty) return;
	    build.head.join
	}
	
}