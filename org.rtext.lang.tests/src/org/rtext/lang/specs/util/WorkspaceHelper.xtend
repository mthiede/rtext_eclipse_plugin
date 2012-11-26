package org.rtext.lang.specs.util

import java.io.File
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IMarker
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IProjectDescription
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.ui.texteditor.MarkerUtilities
import org.eclipse.xtend.lib.Property
import org.eclipse.xtext.xbase.lib.Procedures$Procedure1
import org.junit.After

class WorkspaceHelper {
	
	@Property val workspace = ResourcesPlugin::workspace
	
	val linkedProjects = <IProject>newArrayList
	val createdProjects = <IProject>newArrayList
	
	def createProject(String name){
		val description = workspace.newProjectDescription(name)
		createdProjects += name.doCreateProject(description)
	}
	
	def createProject(String name, Procedures$Procedure1<ProjectInitializer> init){
		val description = workspace.newProjectDescription(name)
		val project = name.doCreateProject(description)
		createdProjects += project
		val initializer = new ProjectInitializer
		init.apply(initializer)
		initializer.apply(project)
		project
				
	}
	
	def createProject(String name, String folder2Link){
		val description = workspace.newProjectDescription(name)
		description.setLocationURI(new File(folder2Link).toURI)
		linkedProjects += name.doCreateProject(description)
	}
	
	def private doCreateProject(String name, IProjectDescription description){
		val IProject project = workspace.root.getProject(name)
		project.create(description, monitor)
		project.open(monitor)
		return project
	}
	
	def project(String name){
		workspace.root.getProject(name)
	}
	
	def file(String path){
		workspace.root.getFile(new Path(path))
	}
	
	def createFolder(String path){
		workspace.root.getFolder(new Path(path)).create(true, true, monitor)
	}
	
	def append(IFile file, CharSequence content){
		file.appendContents(new StringInputStream(content.toString), true, false, monitor)
	}
	
	def delete(IFile file){
		file.delete(true, monitor)
	}
	
	def void writeToFile(CharSequence sequence, String name) {
		name.file.create(new StringInputStream(sequence.toString), true, monitor)
	}

	def findProblems(IFile file){
		val depth = IResource::DEPTH_INFINITE
		file.findMarkers(IMarker::PROBLEM, true, depth).map[MarkerUtilities::getMessage(it)]
	}
	
	@After def cleanUpWorkspace() throws Exception{
		linkedProjects.forEach[delete(false, true, monitor)]
		createdProjects.forEach[delete(true, true, monitor)]
	}
	
	def monitor(){new NullProgressMonitor}
}