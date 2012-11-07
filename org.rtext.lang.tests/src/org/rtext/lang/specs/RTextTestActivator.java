package org.rtext.lang.specs;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class RTextTestActivator extends Plugin {

	private static RTextTestActivator INSTANCE = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}
	
	public static RTextTestActivator getDefault() {
		return INSTANCE;
	}
}
