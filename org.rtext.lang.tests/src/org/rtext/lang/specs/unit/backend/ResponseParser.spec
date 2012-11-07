package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend2.ResponseParser
import org.rtext.lang.backend2.Progress
import org.rtext.lang.specs.util.TestCallBack

describe ResponseParser {
	val callback = new TestCallBack
	
	fact "parses generic response"{
		'{"type":"response", "invocation_id":111, "problems":[], "total_problems":0}'.parse
		callback.response => [
			type => "response"
			invocationId => 111
		]
	}
	
	fact "parses progress"{
		'{"type":"progress","invocation_id":111,"percentage":100}'.parse
		callback.progress.first => [
			it => typeof(Progress)
			type => "progress"
		]
	}
	
	def parse(String input){
		subject.parse(input, callback)
	}

}