package org.rtext.lang.specs.unit.util

import org.rtext.lang.util.Strings

describe Strings{
	def {
		| input 				| result 						|
		| "ruby"				| list("ruby")					|
		| "ruby -l"				| list("ruby", "-l")			|
		| "ruby  -l"			| list("ruby", "-l")			|
		| 'ruby -l "arg"'		| list("ruby", "-l", "arg")		|
	}
	
	fact examples.forEach[Strings::splitCommand(input).toList => result]
}