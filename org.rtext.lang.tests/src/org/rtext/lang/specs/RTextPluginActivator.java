package org.rtext.lang.specs;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.rtext.lang.backend2.CachingConnectorProvider;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ConnectorProvider;

public class RTextPluginActivator extends Plugin {

	private static RTextPluginActivator INSTANCE = null;
	private ConnectorProvider connectorProvider;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}
	
	public static RTextPluginActivator getDefault() {
		return INSTANCE;
	}
	
	public Connector getConnector(String modelFilePath){
		if(connectorProvider == null){
			connectorProvider = CachingConnectorProvider.create();
		}
		return connectorProvider.get(modelFilePath);
	}
}
