/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.rtext.lang.util.FileLocator;

public class OpenEditorHelper {

	public static void openInRTextEditor(IWorkbenchPage workbenchPage, String filename, int line) {
		List<IFile> files = new FileLocator().locate(filename);
		if(files.isEmpty()){
			return;
		}
		IFile file = files.get(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IMarker.LINE_NUMBER, new Integer(line));
		map.put(IDE.EDITOR_ID_ATTR, "org.rtext.lang.Editor");
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

	public static IWorkspaceRoot workspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
}
