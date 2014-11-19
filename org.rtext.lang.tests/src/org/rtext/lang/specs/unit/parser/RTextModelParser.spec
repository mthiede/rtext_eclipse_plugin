/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.specs.unit.parser

import java.util.List
import org.rtext.lang.model.Element
import org.rtext.lang.model.RTextModelParser
import org.rtext.lang.specs.util.SimpleDocument

import static org.rtext.lang.specs.util.IsElement.*

describe RTextModelParser {
	SimpleDocument document
	
	describe "structure"{
	
		fact "parse command"{
			"Type".parse.first 			=> element("Type")
			"OtherType".parse.first		=> element("OtherType")
		}
		
		fact "parse command with annotation"{
			"@file-extension: ecuconfig\nType".parse.first 			=> element("Type")
		}
		
		fact "parse multiple root elements"{
			'''
			Root1
			Root2
			'''.parse.size => 2
		}
		
		fact "correctly sets offset of last element"{
			val input = '''
			Root1
			Root2
			
			'''
			input.parse.size => 2
		}
				
		fact "parse name"{
			"Type name1".parse.first 					=> element("Type", "name1")
			"OtherType name2".parse.first 				=> element("OtherType", "name2")
			"Type name3 label: something".parse.first 	=> element("Type", "name3")
		}
		
		fact "parse attributes"{
			"Type name1, label: 10".parse.first => 	element("Type", "name1")
			'Type name1, label: "a string"'.parse.first => 	element("Type", "name1")
			'Type name1, label: /a/reference'.parse.first => element("Type", "name1")
		}
		fact "ignores comments"{
			'''
			# a comment
			Type'''.parse.size => 1
		}
		
		fact "nests list"{
			'''
			Type parent{
				Child child1{
				}
				Child child2{
				}
			}
			'''.parse.first => 
				element("Type", "parent", 
					element("Child", "child1"),
					element("Child", "child2")
				)
		}
		
		fact "nests list without curly braces"{
			'''
			Type parent{
				Child child1
				Child child2
			}
			'''.parse.first => 
				element("Type", "parent", 
					element("Child", "child1"),
					element("Child", "child2")
				)
		}

	}

	describe "full text region"{
		fact "spans type and name"{
			var input = "Type parent"
			var region = input.parse.first.fullText(input)
			region => input
		}
		
		fact "spans over children"{
			var input = '''
				Type{
					Child{
					}
					Child{
					}
				}'''
			var content = input.parse.first.fullText(input)
			content => input.toString
		}
		
		fact "spans children with feature def"{
			val input = '''
			IntegerType myType { 
				lowerLimit: [
					ARLimit   
				]
			}'''
			var content = input.parse.first.fullText(input)
			content => input.toString
		}
		
		fact "child element region spans all child childs"{
			val input = '''
				Type parent{
					Child child{
						ChildChild childChild
					}
				}
			'''
			val content = input.parse.first.children.first.fullText(input)
			content => '''
			Child child{
					ChildChild childChild
				}'''.toString
		}
		
		fact "deeply nested elements"{
			val input = '''
				Type parent{
					Child child{
						ChildChild childChild{
							ChildChildChild childChildChild	{
								X x
							}
						}
					}
					
				}
			'''
			val region = input.elements("childChildChild").fullText(input)
			region => '''
			ChildChildChild childChildChild	{
							X x
						}'''.toString
		}
	}
	
	describe "error handling"{
		fact "ignores too many closing curly braces"{
			val input = '''
				Type parent{
					Child
					}					
				}
			'''
			input.parse.first => element("Type", "parent",
				element("Child")
			)
		}
		
		fact "creates no elements for empty string"{
			"".parse.size => 0
		}
	}
	
	def fullText(Element element, CharSequence s){
		document.get(element.position.offset, element.position.length)		
	}
	
	def elements(CharSequence input, String name){
		input.parse.find(name)
	}
	
	def Element find(List<Element> elements, String name){
		var result = elements.findFirst[candidate | candidate.name == name ]
		if(result != null) return result
		for(candidate : elements){
			if(candidate.name == name){
				return candidate
			}
			result = candidate.children.find(name)
			if(result != null){
				return result
			}
		}
		return null
	}
	
	def parse(CharSequence s){
		document = new SimpleDocument(s.toString)
		subject.setRange(document, 0, s.length)
		subject.parse		
	}

}