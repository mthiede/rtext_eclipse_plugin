/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang;

import java.util.ResourceBundle;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rtext.lang.backend.CachingConnectorProvider;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.workspace.RTextFileChangeListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class RTextPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.rtext.lang.editor";

	private static RTextPlugin plugin;

	private ResourceBundle resourceBundle;

	private ConnectorProvider connectorProvider;

	private RTextFileChangeListener rtextFileChangeListener;

	public RTextPlugin() {
		resourceBundle = ResourceBundle
				.getBundle("org.rtext.lang.RTextPluginResources");
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		startListeningForRTextFileChanges();
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		stopListeningForRTextFileChanges();
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

	public Connector getConnector(String modelFilePath) {
		return getConnectorProvider().get(modelFilePath);
	}

	public ConnectorProvider getConnectorProvider() {
		if (connectorProvider == null) {
			connectorProvider = CachingConnectorProvider.create();
		}
		return connectorProvider;
	}

	public static void logError(String message, Throwable e) {
		getDefault().getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, message, e));
	}

	public void startListeningForRTextFileChanges() {
		rtextFileChangeListener = RTextFileChangeListener.create(getConnectorProvider());
		workspace().addResourceChangeListener(rtextFileChangeListener, IResourceChangeEvent.POST_CHANGE);
	}
	
	public void stopListeningForRTextFileChanges() {
		workspace().removeResourceChangeListener(rtextFileChangeListener);
	}

	public IWorkspace workspace() {
		return ResourcesPlugin.getWorkspace();
	}
}
