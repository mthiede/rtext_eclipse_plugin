package org.rtext.lang.specs.util;

import java.util.List;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.rtext.lang.commands.LoadModelCallback;

@SuppressWarnings("all")
public class Jobs {
  public static void waitForRTextJobs() {
    try {
      final IJobManager jobMan = Job.getJobManager();
      final Job[] build = jobMan.find(LoadModelCallback.RTEXT_JOB_FAMILY);
      boolean _isEmpty = ((List<Job>)Conversions.doWrapArray(build)).isEmpty();
      if (_isEmpty) {
        return;
      }
      Job _head = IterableExtensions.<Job>head(((Iterable<Job>)Conversions.doWrapArray(build)));
      _head.join();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
