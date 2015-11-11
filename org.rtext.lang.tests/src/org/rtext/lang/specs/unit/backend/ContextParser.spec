package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.ContextParser
import org.rtext.lang.specs.util.SimpleDocument
import java.util.ArrayList
import static org.junit.Assert.*

describe ContextParser {

	fact "test_simple" {
		assert_context(
			'''
				A {
				  B {
				    |F bla
			''',
			'''
				A {
				  B {
				    C a1: v1, a2: "v2"
				    D {
				      E a1: 5
				    }
				    |F bla
			''')
	}

	fact "test_child_label"{
		assert_context(
			'''
				A {
				  sub:
				    B {
				      F bla|
			''',
			'''
				A {
				  sub:
				    B {
				      C a1: v1, a2: "v2"
				      D {
				        E a1: 5
				      }
				      F bla|
			''')
	}

	fact "test_child_label_array"{
		assert_context(
			'''
				A {
				  sub: [
				    B {
				      F| bla
			''',
			'''
				A {
				  sub: [
				    B {
				      C
				    }
				    B {
				      C a1: v1, a2: "v2"
				      D {
				        E a1: 5
				      }
				      F| bla
			''')
	}

	fact "test_ignore_child_lables"{
		assert_context(
			'''
				A {
				  B {
				    F bl|a
			''',
			'''
				A {
				  B {
				    sub:
				      C a1: v1, a2: "v2"
				    sub2: [
				      D {
				        E a1: 5
				      }
				    ]
				    F bl|a
			''')
	}

	fact "test_linebreak"{
		assert_context(
			'''
				A {
				  B {
				    C name,      a1: v1,        a2: "v2"|
			''',
			'''
				A {
				  B {
				    C name,
      a1: v1,  
      a2: "v2"|
			''')
	}

	fact "test_linebreak_arg_array"{
		assert_context(
			'''
				A {
				  B {
				    C name,	a1: [         v1,        v2      ],      a2: |5
			''',
			'''
				A {
				  B {
				    C name,
	a1: [ 
        v1,
        v2
      ], 
      a2: |5
			''')
	}

	fact "test_linebreak_empty_last_line"{
		assert_context(
			'''
				A {
				  B name,				    |
			''',
			'''
				A {
				  B name,
				    |
			''')
	}

	fact "test_linebreak_empty_last_line2"{
		assert_context(
			'''
				A {
				  B name,				 |
			''',
			'''
				A {
				  B name,
				 |
			  ''')
	}

	fact "test_linebreak_empty_lines"{
		assert_context(
			'''
				A {
				  B name, 	  a1: |
			''',
			'''
				A {
				  B name, 
 	
  a1: |
			''')
	}

	fact "test_comment_annotation"{
		assert_context(
			'''
				A {
				  B {
				    |F bla
			''',
			'''
				A {
				  # bla
				  B {
				    C a1: v1, a2: "v2"
				    # bla
				    D {
				      E a1: 5
				    }
				    @ anno
				    |F bla
			''')
	}
	
	fact "test_line_break"{
		assert_context('''
		EPackage StatemachineMM,  cnsURI: "",		|''',
		'''
		EPackage StatemachineMM,  cnsURI: "", 
		|
		'''
		)
	}

	def assert_context(String expected, String actual) {
		val exp_lines = new ArrayList<String>(expected.split("\n"))
		val exp_col = exp_lines.last.indexOf("|") + 1

		exp_lines += exp_lines.remove(exp_lines.length - 1).replace("|", "")
		val ctx = parse(actual.trim.replace("|", ""), actual.indexOf("|"))
		assertEquals(exp_lines, ctx.parseContext)
		assertEquals(exp_col, ctx.column)
	}

	def parse(String text, int col) {
		val document = new SimpleDocument(text)
		val parser = new ContextParser(document)
		parser.getContext(col)
	}

}
