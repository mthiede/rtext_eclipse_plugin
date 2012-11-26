package org.rtext.lang.specs.unit.workspace

import java.io.File
import org.eclipse.core.resources.IProject
import org.rtext.lang.backend.ConnectorConfig
import org.rtext.lang.backend.ConnectorScope
import org.rtext.lang.specs.util.WorkspaceHelper

import static org.jnario.lib.JnarioCollectionLiterals.*

import static extension org.jnario.lib.Should.*

describe ConnectorScope{
	
	extension WorkspaceHelper = new WorkspaceHelper
	IProject project
	
	fact "Contains files in same directory"{
		project = createProject("test")[
			file("file1.aaa", "content")
			file("file2.bbb", "content")
		]
		scope(".rtext") => list("file1.aaa")
	}
	
	fact "Contains files in subdirectory directory"{
		project = createProject("test")[
			folder("folder")
			file("folder/file1.aaa", "content")
			file("folder/file2.bbb", "content")
		]
		scope(".rtext") => list("file1.aaa")
	}
	
	fact "Contains files in project"{
		project = createProject("test")[
			file("file1.aaa", "content")
			file("file2.bbb", "content")
		]
		scope("../../.rtext") => list("file1.aaa")
	}
	
	fact "Contains files in multiple projects"{
		project = createProject("test1")[
			file("file1.aaa", "content")
			file("file2.bbb", "content")
		]
		createProject("test2")[
			file("file3.aaa", "content")
			file("file4.bbb", "content")
		]
		scope("../../.rtext") => list("file1.aaa", "file3.aaa")
	}
	
	def scope(String path){
		var location = project.location.toFile
		location = new File(location.toString + "/" + path)
		val scope = ConnectorScope::create(new ConnectorConfig(location, "", "*.aaa"))
		val scopedElements = <String>newArrayList
		scope.forEach[scopedElements+=name]
		scopedElements
	}
}