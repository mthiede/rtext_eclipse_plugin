/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang;

import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rtext.lang.backend.ConnectorManager;
import org.rtext.lang.backend2.CachingConnectorProvider;
import org.rtext.lang.backend2.Connector;
import org.rtext.lang.backend2.ConnectorProvider;


/**
 * The activator class controls the plug-in life cycle
 */
public class RTextPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.rtext.lang.editor";

	private static RTextPlugin plugin;
	
	private ResourceBundle resourceBundle;

	private ConnectorProvider connectorProvider;

	public RTextPlugin() {
		resourceBundle = ResourceBundle.getBundle("org.rtext.lang.RTextPluginResources");
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ConnectorManager.start();
	}

	public void stop(BundleContext context) throws Exception {
		ConnectorManager.stop();
		plugin = null;
		super.stop(context);
	}

	public static RTextPlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public Connector getConnector(String modelFilePath){
		if(connectorProvider == null){
			connectorProvider = CachingConnectorProvider.create();
		}
		return connectorProvider.get(modelFilePath);
	}

	public ConnectorProvider getConnectorProvider() {
		return connectorProvider;
	}
}
