package org.rtext.lang.specs.unit.backend

import static org.junit.Assert.*
import org.rtext.lang.commands.CommandSerializer
import org.rtext.lang.commands.Command
import org.rtext.lang.commands.Response

describe "Converting Commands to JSON"{

	val command = "load_model"
	val type = "request"
	val invocationId = 42

	extension CommandSerializer serializer = new CommandSerializer
	fact "converts command to json"{
		new Command<Response>(invocationId, type, command, typeof(Response)).serialize.is('''
		{
		    "type": "«type»",
		    "command": "«command»",
		    "invocation_id": «invocationId»
		}
		''')
	}
	
	def is(String actual, CharSequence expected){
		var text = expected.toString.replaceAll("\\s", "")
		text = text.length + text
		assertEquals(text, actual)
	}
}
