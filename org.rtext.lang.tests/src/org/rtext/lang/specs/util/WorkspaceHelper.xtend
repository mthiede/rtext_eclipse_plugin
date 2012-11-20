package org.rtext.lang.specs.util

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import java.net.URI
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.ui.ide.undo.CreateProjectOperation

class WorkspaceHelper {
	
	val workspace = ResourcesPlugin::workspace
	
	def createProject(String name, String folder2Link){
		val project = project(name)
		val description = workspace.newProjectDescription(project.getName())
		description.setLocationURI(URI::create(folder2Link))
		new CreateProjectOperation(description, "").execute(new NullProgressMonitor, null)
	}
	
	def project(String name){
		workspace.root.getProject(name)
	}
	
	def file(String path){
		workspace.root.getFile(new Path(path))
	}
	
}