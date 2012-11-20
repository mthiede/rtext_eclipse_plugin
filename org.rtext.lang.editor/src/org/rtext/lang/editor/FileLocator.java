package org.rtext.lang.editor;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

public class FileLocator {
	public List<IFile> locate(String path){
		return Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(path)));
	}
	
}
