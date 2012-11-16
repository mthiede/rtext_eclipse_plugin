package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.editor.ContentAssistProcessor
import java.util.List

Feature: Code completion
	
Scenario: Sucessfully using code completion
	extension BackendHelper b = new BackendHelper
	String modelFile
	List<String> proposals
	ContentAssistProcessor proposalProvider
	
	Given a backend for "rtext/test/integration/model/test_metamodel.ect"
		b.startBackendFor(args.first)
	When I invoke the code completion after "EPackage StatemachineMM {\n"
		proposalProvider = ContentAssistProcessor::create([|b.connector]) 
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
	proposals => expectedProposals
	
Scenario: Proposal signals backend failure
	
	Given a backend for "rtext/test/integration/model/test.crashing_backend"
	When I invoke the code completion after "EPackage StatemachineMM {\n"
	Then the proposals should be
	'''
		Cannot load backend
	'''