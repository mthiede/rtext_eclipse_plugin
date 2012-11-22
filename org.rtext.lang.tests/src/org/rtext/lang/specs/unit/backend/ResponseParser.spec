package org.rtext.lang.specs.unit.backend

import org.rtext.lang.commands.ResponseParser
import org.rtext.lang.commands.Progress
import org.rtext.lang.specs.util.TestCallBack
import org.rtext.lang.commands.Response

describe ResponseParser {
	TestCallBack<? extends Response> callback 
	
	fact "parses generic response"{
		'{"type":"response", "invocation_id":111, "problems":[], "total_problems":0}'.parse(typeof(Response))
		callback.response => [
			type => "response"
			invocationId => 111
		]
	}
	
	fact "parses progress"{
		'{"type":"progress","invocation_id":111,"percentage":100}'.parse(typeof(Response))
		callback.progress.first => [
			it => typeof(Progress)
			type => "progress"
		]
	}
	
	fact "parses proposal response type"{
		'{"type":"response","invocation_id":1,"options":[{"insert":"EAnnotation","display":"EAnnotation "},{"insert":"EClass","display":"EClass <name>"}]}'
	}
	
	def <T extends Response> parse(String input, Class<T> responseType){
		val callback = new TestCallBack<T>
		subject.parse(input, callback, responseType)
		this.callback = callback
	}

}