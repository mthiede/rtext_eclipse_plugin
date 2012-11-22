package org.rtext.lang.specs.util

import java.net.URI
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IMarker
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.ui.texteditor.MarkerUtilities
import org.junit.Before

class WorkspaceHelper {
	
	val workspace = ResourcesPlugin::workspace
	
	def createProject(String name, String folder2Link){
		val description = workspace.newProjectDescription(name)
		description.setLocationURI(URI::create(folder2Link))
		val IProject project = workspace.root.getProject(name)
		project.create(description, new NullProgressMonitor)
		project.open(new NullProgressMonitor)
	}
	
	def project(String name){
		workspace.root.getProject(name)
	}
	
	def file(String path){
		workspace.root.getFile(new Path(path))
	}

	def findProblems(IFile file){
		val depth = IResource::DEPTH_INFINITE
		file.findMarkers(IMarker::PROBLEM, true, depth).map[MarkerUtilities::getMessage(it)]
	}
	
	@Before def cleanUpWorkspace(){
		workspace.root.projects.forEach[
			try{
				delete(false, true, new NullProgressMonitor)
			}catch(Exception e){
				e.printStackTrace
			}
		]
	}
	
}