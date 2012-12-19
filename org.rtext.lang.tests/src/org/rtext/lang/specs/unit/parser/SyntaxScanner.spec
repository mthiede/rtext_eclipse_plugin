/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.specs.unit.parser

import org.eclipse.jface.text.TextAttribute
import org.eclipse.jface.text.rules.IToken
import org.rtext.lang.editor.ColorManager
import org.rtext.lang.editor.SyntaxScanner
import org.rtext.lang.specs.util.SimpleDocument

import static org.jnario.lib.JnarioCollectionLiterals.*
import static org.rtext.lang.editor.IColorConstants.*
import static org.rtext.lang.model.AbstractRTextParser.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*

describe SyntaxScanner {
 	
	before subject = new SyntaxScanner(new ColorManager)
	
	fact "parse comments"{
		'''
		#a comment
		'''.scan.first => COMMENT
	}
	 
	fact "parse annotations"{
		'''
		@a comment
		'''.scan.first => ANNOTATION
	}
	
	fact "parse comments until EOL"{
		("#a comment" + EOL).scan.first => COMMENT
	}

	fact "parse command"{
		'''
		Type name, label: /a/Reference
		'''.scan.first => COMMAND
	}
	
	fact "parse command starting with '_'"{
		'''
		Type _name, label: /a/Reference
		'''.scan.first => COMMAND
	}
	
	fact "parse identifier"{
		'''
		Type name, label: /a/Reference
		'''.scan.second => IDENTIFIER
	}
	
	fact "parse identifier with '_'"{
		'''
		Type name_with, label: /a/Reference
		'''.scan.second => IDENTIFIER
	}
	
	fact "parse separator"{
		'''
		Type name, label: /a/Reference
		'''.scan.third => DEFAULT
	}
	
	
	fact "parse label"{
		'''
		Type name, label: /a/Reference
		'''.scan.get(5) => LABEL
	}
	
	fact "parse reference"{
		'''
		Type name, label: /long/a/Reference
		'''.scan.get(7) => REFERENCE
	}
	
	fact "parse macros"{
		'''
		Type name, label: <generic>
		'''.scan.get(7) => GENERICS
	}
	
	fact "parse reference without leading '/'"{
		'''
		Type name, label: a/long/Reference
		'''.scan.get(7) => REFERENCE
	}
	
	fact "parse number"{
		'''
		Type name, label: 8
		'''.scan.get(7) => NUMBER
	}
	
	fact "parse string"{
		'''
		Type name, label: "a string"
		'''.scan.get(7) => STRING
	}
	
	fact "parse enum"{
		'''
		Type name, label: enum
		'''.scan.get(7) => IDENTIFIER
	}
	
	fact "parse whitespace"{
		"Type name, label: enum\r\n".scan.fifth => IDENTIFIER
	}
	
	fact "parse whitespace after command"{
		val input = "Type   "
		input.scan
		assert subject.tokenOffset + subject.tokenLength <= input.length
	}
	
	fact "parse string until EOL"{
		('Type name, label: "a string ' + EOL).scan.get(7) => STRING
	}
	
	fact "positions macros correctly"{
		'''
		EPackage{
			EClass{
				EOperation{
					EAnnotation source:<source> 
				}
			}
		}	

		'''.regions.get(12) => "<source>"
	} 
	
	fact "support escaped macros"{
		'''
		EPackage{
			EClass{
				EOperation{
					EAnnotation source:<%text>text%> 
				}
			}
		}	

		'''.regions.get(12) => "<%text>text%>"
	} 
	
	fact "support escaped macros without end"{
		val region = '''
		EPackage{
			EClass{
				EOperation{
					EAnnotation source:<%text>text'''.regions.get(12) 
		region => "<%text>text"
	} 

	def regions(CharSequence s){
		val document = new SimpleDocument(s.toString)
		subject.setRange(document, 0, s.length)
		
		val regions = <String>list
		var token = subject.nextToken()
		while (!token.isEOF()) {
			regions += document.get(subject.tokenOffset, subject.tokenLength)
			token= subject.nextToken()
		}
		regions
	}
	
	
	def scan(CharSequence s){
		val document = new SimpleDocument(s.toString)
		subject.setRange(document, 0, s.length)
		
		val tokens = <IToken>list
		var token= subject.nextToken()
		while (!token.isEOF()) {
			tokens += token
			token= subject.nextToken()
		}
		tokens.map[
			val attr = it.data as TextAttribute
			attr.foreground.RGB
		]
	}
} 