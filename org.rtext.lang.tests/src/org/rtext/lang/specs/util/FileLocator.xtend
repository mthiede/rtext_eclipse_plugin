package org.rtext.lang.specs.util

import org.rtext.lang.specs.RTextPluginActivator
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.FileLocator
import java.io.File
import org.eclipse.core.runtime.Platform

@Data
class TestFileLocator {
	
	def static getDefault(){
		new TestFileLocator("backends/head")
	}
	
	String rootFolder
	
	def private file(String relativePath){
		if(Platform::running){
			val fullpath = relativePath.toFullPath
			val url = RTextPluginActivator::getDefault().find(fullpath)
			val fileUrl = FileLocator::toFileURL(url)
			new File(fileUrl.toURI)
		}else{
			new File(relativePath.resolve)
		}
	}
	
	def getRoot(){
		"".absolutPath
	}
	
	def absolutPath(String relativePath){
		relativePath.file.absolutePath
	}
	
	def private toFullPath(String relativePath){
		new Path(relativePath.resolve)
	}
	
	def private resolve(String relativePath){
		rootFolder + "/" + relativePath
	}
	
}