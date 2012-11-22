package org.rtext.lang.specs.unit.util

import org.rtext.lang.util.Files
import static extension org.rtext.lang.util.Files.*
import java.io.File

describe Files {
	
	context "fileExtension"{
		fact ^extension(null) throws IllegalArgumentException
		def {
			| filename				| ext 		|
			| ""					| ""		|
			| "file.txt"			| "*.txt"	|
			| "file.txt"			| "*.txt"	|
			| "test.file.txt"		| "*.txt"	|
			| "txt"					| "txt"		|
			| "/folder/file.txt"	| "*.txt"	|
			| ".txt"				| "*.txt"	|
			| "/folder/.txt"		| "*.txt"	|
			| "\\folder\\.txt"		| "*.txt"	|
			| "\\folder\\txt"		| "txt"	|
		}
		fact examples.forEach[new File(filename).^extension => ext]			
	}
} 