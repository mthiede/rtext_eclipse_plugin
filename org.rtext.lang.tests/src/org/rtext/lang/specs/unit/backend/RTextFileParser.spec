package org.rtext.lang.specs.unit.backend

import org.eclipse.xtext.xbase.lib.Pair
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.rtext.lang.backend.RTextFileParser
import org.rtext.lang.backend.RTextFile

import static org.rtext.lang.specs.util.Files.*

describe RTextFileParser{

	@Rule extension TemporaryFolder = new TemporaryFolder 

	facts "File specific commands are defined by: 'FILE_PATTERN:\nCOMMAND'"{
		'''
		*.ect:
		the command
		'''.parse => [
			it should contain "ect" -> "the command"
			it should not contain "unknown" -> ""
		]
	}
	
	facts "Multiple commands are separated by newlines"{
		'''
		*.ect:
		the command
		*.abc:
		other command
		'''.parse => [
			it should contain "ect" -> "the command"
			it should contain "abc" -> "other command"
		]
	}
	
	facts "Handles whitespace at the end"{
		'''
		*.ect: 
		the command
		'''.parse => [
			it should contain "ect" -> "the command"
		]
	}

	def should_contain(RTextFile file, Pair<String, String> command){
		val etc = file.getConfiguration("*." + command.key)
		if(etc == null){
			return false
		}
		return etc.command == command.value
	}
	
	def parse(CharSequence s){
		subject.doParse(newFileWithContent(newFile("input.txt"), s))
	}
}