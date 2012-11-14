package org.rtext.lang.specs.util

import java.io.File
import java.io.PrintWriter
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.nio.charset.Charset

class Files {
	
	def static read(String path){
		val stream = new FileInputStream(new File(path));
	  try {
	    val fc = stream.getChannel();
	    val bb = fc.map(FileChannel$MapMode::READ_ONLY, 0, fc.size());
	    /* Instead of using default, pass in a decoder. */
	    return Charset::defaultCharset().decode(bb).toString();
	  }
	  finally {
	    stream.close();
	  }
	}
	
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