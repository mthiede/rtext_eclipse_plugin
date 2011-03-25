package rtext.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

public class OpenEditorHelper {
	
	public static void openInRTextEditor(IWorkbenchPage workbenchPage, String filename, int line) {
		IPath path = new Path(filename);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IMarker.LINE_NUMBER, new Integer(line));
		map.put(IDE.EDITOR_ID_ATTR,	"rtext.Editor");
		IMarker marker = null;
		try {
			marker = file.createMarker(IMarker.TEXT);
			marker.setAttributes(map);
			IDE.openEditor(workbenchPage, marker);
		} catch (PartInitException e) {
		} catch (CoreException e1) {
		}
		finally {
			if (marker != null) {
				try {
					marker.delete();
				} catch (CoreException e) {
				}
			}
		}
	}
}
