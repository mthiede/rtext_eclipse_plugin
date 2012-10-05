package rtext

import static org.rtext.model.ElementBuilder.*
import org.rtext.model.RTextModelParser
import org.rtext.model.ElementBuilder

describe RTextModelParser {
	
	fact "parse command"{
		"Type".parse 			=> elements(element("Type").offset(0).length(4))
		"OtherType".parse		=> elements(element("OtherType").offset(0).length(9))
	}
	
	fact "parse name"{
		"Type name1".parse 		=> elements(element("Type").name("name1").offset(0).length(10))
		"OtherType name2".parse => elements(element("OtherType").name("name2").offset(0).length(15))
	}
	
	fact "parse attributes"{
		"Type name1, label: 10".parse => elements(
			element("Type").name("name1").offset(0).length(21)
		)
		'Type name1, label: "a string"'.parse => elements(
			element("Type").name("name1").offset(0).length(29)
		)
		'Type name1, label: /a/reference'.parse => elements(
			element("Type").name("name1").offset(0).length(31)
		)
	}
	
	fact "ignores comments"{
		'''
		# a comment
		Type'''.parse	=> elements(element("Type").offset(11).length(5))
	}
	
	fact "nests elements"{
		'''
		Type parent{
			Child child1{
			}
			Child child2{
			}
		}
		'''.parse => elements(
			element("Type").name("parent").offset(0).length(12).children(
				element("Child").name("child1").offset(12).length(18),
				element("Child").name("child2").offset(30).length(20)
			)
		)
	}
	
	fact "nests elements without curly braces"{
		'''
		Type parent{
			Child child1
			Child child2
		}
		'''.parse => elements(
			element("Type").name("parent").offset(0).length(12).children(
				element("Child").name("child1").offset(12).length(14),
				element("Child").name("child2").offset(26).length(16)
			)
		)
	}
	
	def elements(ElementBuilder... elements){
		elements.map[build]
	}
	
	def parse(CharSequence s){
		val document = new SimpleDocument(s.toString)
		subject.setRange(document, 0, s.length)
		subject.parse		
	}

}