package org.rtext.lang.specs.util

import java.io.File
import java.io.PrintWriter

class Files {
	def static newFile(File folder, String name){
		val file = new File(folder + File::separator + name)
		file.mkdirs
		file.createNewFile
		file
	}
	
	def static newFileWithContent(File file, CharSequence contents){
		val writer = new PrintWriter(file)
		writer.write(contents.toString)
		writer.close
		file
	}
}