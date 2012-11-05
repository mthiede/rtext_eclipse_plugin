/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.specs.unit

import org.eclipse.jface.text.TextAttribute
import org.eclipse.jface.text.rules.IToken
import org.rtext.lang.editor.ColorManager
import org.rtext.lang.editor.SyntaxScanner

import static org.jnario.lib.JnarioCollectionLiterals.*
import static org.rtext.lang.model.AbstractRTextParser.*
import static org.rtext.lang.editor.IColorConstants.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*
import org.rtext.lang.specs.util.SimpleDocument

describe SyntaxScanner {
 	
	before subject = new SyntaxScanner(new ColorManager)
	
	fact "parse comments"{
		'''
		#a comment
		'''.scan.first => COMMENT
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
		'''.scan.forth => LABEL
	}
	
	fact "parse reference"{
		'''
		Type name, label: /long/a/Reference
		'''.scan.fifth => REFERENCE
	}
	
	fact "parse reference without leading '/'"{
		'''
		Type name, label: a/long/Reference
		'''.scan.fifth => REFERENCE
	}
	
	fact "parse number"{
		'''
		Type name, label: 8
		'''.scan.fifth => NUMBER
	}
	
	fact "parse string"{
		'''
		Type name, label: "a string"
		'''.scan.fifth => STRING
	}
	
	fact "parse enum"{
		'''
		Type name, label: enum
		'''.scan.fifth => IDENTIFIER
	}
	
	fact "parse whitespace"{
		"Type name, label: enum\r\n".scan.fifth => IDENTIFIER
	}
	
	fact "parse string until EOL"{
		('Type name, label: "a string ' + EOL).scan.fifth => STRING
	}
	
	fact "parse nested elements"{
		'''
			AUTOSAR {
				CalprmElementPrototype cpSorolloTMax, type: /AUTOSAR/DataTypes/UInt2 {
					SwDataDefProps swCalibrationAccess: readOnly, swImplPolicy: standard, swVariableAccessImplPolicy: optimized
				} 
				
				CalprmElementPrototype cpSoroTuerTRelax, 	type: /AUTOSAR/DataTypes/UInt4 {
					SwDataDefProps swCalibrationAccess: readOnly, swImplPolicy: standard, swVariableAccessImplPolicy: optimized
				}
			}
		'''.scan => list(COMMAND, DEFAULT, 
							COMMAND, IDENTIFIER, DEFAULT, LABEL, REFERENCE, DEFAULT, 
						 		COMMAND, LABEL, IDENTIFIER, DEFAULT, LABEL, IDENTIFIER, DEFAULT, LABEL, IDENTIFIER,
						 	DEFAULT,
						 	COMMAND, IDENTIFIER, DEFAULT, LABEL, REFERENCE, DEFAULT, 
						 		COMMAND, LABEL, IDENTIFIER, DEFAULT, LABEL, IDENTIFIER, DEFAULT, LABEL, IDENTIFIER,
						 	DEFAULT,
						 DEFAULT)
				
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