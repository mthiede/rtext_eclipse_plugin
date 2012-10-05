package org.rtext.editor;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.rtext.RTextPlugin;

/**
 * @author Sebastian Zarnekow
 */
public class PluginImageHelper implements BundleListener {
	
	private Map<ImageDescriptor, Image> registry = new HashMap<ImageDescriptor, Image>(10);

	private AbstractUIPlugin plugin;

	public PluginImageHelper() {
		this(RTextPlugin.getDefault());
	}
	
	public PluginImageHelper(AbstractUIPlugin plugin) {
		this.plugin = plugin;
		plugin.getBundle().getBundleContext().addBundleListener(this);
	}

	private String pathSuffix = "icons/"; //$NON-NLS-1$

	private String defaultImage = "default.gif"; //$NON-NLS-1$

	private String notFound = "notFound.gif"; //$NON-NLS-1$

	/**
	 * Returns the image associated with the given image descriptor.
	 * 
	 * @param descriptor
	 *            the image descriptor for which the helper manages an image, or <code>null</code> for a missing image
	 *            descriptor
	 * @return the image associated with the image descriptor or <code>null</code> if the image descriptor can't create
	 *         the requested image.
	 */
	public Image getImage(ImageDescriptor descriptor) {
		if (descriptor == null) {
			descriptor = ImageDescriptor.getMissingImageDescriptor();
		}

		Image result = registry.get(descriptor);
		if (result != null) {
			return result;
		}
		result = descriptor.createImage();
		if (result != null) {
			registry.put(descriptor, result);
		}
		return result;
	}

	/**
	 * Disposes all images managed by this image helper.
	 */
	public void dispose() {
		for (Iterator<Image> iter = registry.values().iterator(); iter.hasNext();) {
			Image image = iter.next();
			image.dispose();
		}
		registry.clear();
	}

	public Image getImage(String imageName) {
		String imgname = imageName == null ? defaultImage : imageName;
		if (imgname != null) {
			Image result = null;
			URL imgUrl = getPlugin().getBundle().getEntry(getPathSuffix() + imgname);
			if (imgUrl != null) {
				ImageDescriptor id = null;
				result = getPlugin().getImageRegistry().get(imgUrl.toExternalForm());
				if (result == null) {
					id = ImageDescriptor.createFromURL(imgUrl);
					if (id != null) {
						result = id.createImage();
						getPlugin().getImageRegistry().put(imgUrl.toExternalForm(), result);
					}
				}
				return result;
			}
			if (!imgname.equals(notFound)) {
				return getImage(notFound);
			}
		}
		return null;
	}

	public void setPathSuffix(String pathSuffix) {
		this.pathSuffix = pathSuffix;
	}

	public String getPathSuffix() {
		return pathSuffix;
	}

	public void setPlugin(AbstractUIPlugin plugin) {
		this.plugin = plugin;
	}

	public AbstractUIPlugin getPlugin() {
		return plugin;
	}

	public void setNotFound(String notFound) {
		this.notFound = notFound;
	}

	public String getNotFound() {
		return notFound;
	}

	public String getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}

	public void bundleChanged(BundleEvent event) {
		if (event.getType() == BundleEvent.STOPPING
				&& event.getBundle().getBundleId() == getPlugin().getBundle().getBundleId()) {
			dispose();
		}
	}

}
