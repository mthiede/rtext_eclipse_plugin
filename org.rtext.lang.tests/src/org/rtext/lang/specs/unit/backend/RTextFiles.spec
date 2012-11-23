package org.rtext.lang.specs.unit.backend

import static org.rtext.lang.specs.util.Files.*

import org.rtext.lang.backend.RTextFiles
import org.rtext.lang.specs.util.MockInjector
import org.jnario.runner.CreateWith
import org.rtext.lang.backend.RTextFileParser
import org.mockito.Mock
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import org.rtext.lang.backend.RTextFile

import static org.mockito.Mockito.*
import org.rtext.lang.backend.RTextFileFinder

@CreateWith(typeof(MockInjector))
describe RTextFiles {
	
	File modelFile
	
	File currentConfig
	File currentFolder
	@Mock RTextFile currentRTextFile
	
	File parentConfig
	File parentFolder
	@Mock RTextFile parentRTextFile
	
	File rootConfig
	File rootFolder
	@Mock RTextFile rootRTextFile
	
	@Rule extension TemporaryFolder tempFolder = new TemporaryFolder
	
	@Mock RTextFileParser parser
	RTextFiles fileFinder
	
	before "create filesystem"{
		rootFolder = newFolder("root")
		parentFolder = newFolder("root/parent")
		currentFolder = newFolder("root/parent/current")
		modelFile = newFile(currentFolder, "input.txt")
		
		fileFinder = new RTextFiles(parser, modelFile) 
	}

	context "Finding .rtext files"{
		
		fact "in the same folder"{
			currentConfig = newRTextFile(currentFolder)
			when(parser.doParse(currentConfig)).thenReturn(currentRTextFile)
			fileFinder.first => currentRTextFile
		}
		
		fact "in parent folder"{
			parentConfig = newRTextFile(parentFolder)
			when(parser.doParse(parentConfig)).thenReturn(parentRTextFile)
			fileFinder.first => parentRTextFile
		}
		
		fact "in root folder"{
			rootConfig = newRTextFile(rootFolder)
			when(parser.doParse(rootConfig)).thenReturn(rootRTextFile)		
			fileFinder.first => rootRTextFile
		}
	}
	
	fact "Ignores null"{
		rtextFiles(null).size => 0
	}
	
	fact "Ignores not existing files"{
		rtextFiles(new File("not existing")).size => 0
	}

	def rtextFiles(File file){
		new RTextFileFinder().find(file)
	}
	
	def newRTextFile(File folder){
		newFile(folder, ".rtext")
	}
	
}