package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.editor.ContentAssistProcessor
import java.util.List

Feature: Code completion
	
Background:
	extension BackendHelper b = new BackendHelper
	String modelFile
	List<String> proposals
	ContentAssistProcessor proposalProvider
	
	Given a model file "rtext/test/integration/model/test_metamodel.ect"
		modelFile = args.first
	And a running backend
		b.startBackendFor(modelFile)
	And a proposal provider
		proposalProvider = ContentAssistProcessor::create([|b.connector]) 

Scenario: Sucessfully using code completion
	When I invoke the code completion after "EPackage StatemachineMM {\n"
		proposalProvider.assistSessionStarted(_)
		proposals = proposalProvider.computeCompletionProposals(b.document, b.offsetAfter(args.first), 0).map[displayString.trim]
		proposalProvider.assistSessionEnded(_)
	Then the proposals should be
	'''
		EAnnotation
		EClass <name>
		EClassifier <name>
		EDataType <name>
		EEnum <name>
		EGenericType <name>
		EPackage <name>
	'''
	val expectedProposals = args.first.trim.split("\r?\n").map[trim]
	println(proposals.join("\n"))
	proposals => expectedProposals