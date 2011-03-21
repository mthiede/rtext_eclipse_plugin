package rtext;

import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import rtext.backend.ConnectorManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class RTextPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "rtext";

	// The shared instance
	private static RTextPlugin plugin;
	
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor
	 */
	public RTextPlugin() {
		resourceBundle = ResourceBundle.getBundle("rtext.RTextPluginResources");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ConnectorManager.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		ConnectorManager.stop();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static RTextPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}
