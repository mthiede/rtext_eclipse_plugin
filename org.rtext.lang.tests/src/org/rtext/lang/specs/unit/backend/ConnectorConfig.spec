package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.ConnectorConfig

import static org.jnario.lib.JnarioCollectionLiterals.*

import static extension com.google.common.collect.Iterables.*
import static extension org.jnario.lib.ExampleTableIterators.*
import static extension org.jnario.lib.Should.*

describe ConnectorConfig {
	
	context "matches"{
		def {
			| fileName 		| patterns 		| doesMatch |
			| "a.txt"	 	| list("*.txt") | true		|
			| "a.txt1"	 	| list("*.txt") | false		|
			| "a.txt"	 	| list("*.txt") | true		|
		}
		fact examples.forEach[
			val config = new ConnectorConfig(
				_,
				"anyCommand",
				patterns.toArray(typeof(String))
			)
			config.matches(fileName) => doesMatch
		]
	}

}