package org.rtext.lang.util;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Workbenches {

	public static IWorkbenchPage getActivePage() {
		IWorkbench wb = PlatformUI.getWorkbench();
		if(wb == null){
			return null;
		}
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		if(win == null){
			return null;
		}
		return win.getActivePage();
	}
}
