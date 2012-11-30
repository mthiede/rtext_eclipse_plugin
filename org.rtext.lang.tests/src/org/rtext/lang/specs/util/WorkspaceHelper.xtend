package org.rtext.lang.specs.util

import java.io.File
import org.eclipse.core.resources.IContainer
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
import org.eclipse.xtext.xbase.lib.Pair
import org.eclipse.xtext.xbase.lib.Procedures$Procedure1
import org.junit.Before
import org.rtext.lang.RTextPlugin

class ProjectInitializer implements Procedure1<IContainer> {
	
	WorkspaceHelper helper = new WorkspaceHelper
	
	val files = <Pair<String, CharSequence>>newArrayList
	val folders = <String>newArrayList
	
	override apply(IContainer p) {
		folders.forEach[
			helper.createFolder(p.name + "/" + it)
		]
		files.forEach[
			val fileName = p.name + "/" + key
			helper.writeToFile(value, fileName)
		]
	}
	
	def file(String name, CharSequence contents){
		files += name -> contents
	}
	
	def folder(String name){
		folders += name
	}
	
}

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
	
	def createFile(String name, CharSequence content){
		content.writeToFile(name)
	}
	
	
	def createFolder(String path){
		workspace.root.getFolder(new Path(path)).create(true, true, monitor)
	}
	
	def append(IFile file, CharSequence content){
		file.appendContents(new StringInputStream(content.toString), true, false, monitor)
		file
	}
	
	def delete(IFile file){
		file.delete(true, monitor)
	}
	
	def writeToFile(CharSequence sequence, String name) {
		val newFile = name.file
		newFile.create(new StringInputStream(sequence.toString), true, monitor)
		newFile
	}

	def findProblems(IFile file){
		val depth = IResource::DEPTH_INFINITE
		file.findMarkers(IMarker::PROBLEM, true, depth).map[MarkerUtilities::getMessage(it)]
	}
	
	
	@Before def cleanUpWorkspace() throws Exception{
		RTextPlugin::getDefault.connectorProvider.dispose
		workspace.root.projects.forEach[
			if(workspace.root.location.isPrefixOf(it.location)){
				delete(true, true, monitor)
			}
			else{
				delete(false, true, monitor)
			}
		]
	}
	
	def monitor(){new NullProgressMonitor}
}