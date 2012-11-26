package org.rtext.lang.specs;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class RTextPluginActivator extends Plugin {

	private static RTextPluginActivator INSTANCE = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}
	
	public static RTextPluginActivator getDefault() {
		return INSTANCE;
	}
}
