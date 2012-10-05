package org.rtext.editor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

public class OpenEditorHelper {

	public static void openInRTextEditor(IWorkbenchPage workbenchPage,
			String filename, int line) {
		IFile[] files;
		try {
			files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(
					new URI("file://"+filename));
			if (files != null && files.length > 0) {
				IFile file = files[0];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(IMarker.LINE_NUMBER, new Integer(line));
				map.put(IDE.EDITOR_ID_ATTR, "org.rtext.Editor");
				IMarker marker = null;
				try {
					marker = file.createMarker(IMarker.TEXT);
					marker.setAttributes(map);
					IDE.openEditor(workbenchPage, marker);
				} catch (PartInitException e) {
				} catch (CoreException e1) {
				} finally {
					if (marker != null) {
						try {
							marker.delete();
						} catch (CoreException e) {
						}
					}
				}
			}
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}
