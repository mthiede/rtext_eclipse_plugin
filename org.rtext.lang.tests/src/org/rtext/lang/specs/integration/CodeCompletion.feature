package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.proposals.ContentAssistProcessor
import java.util.List
import org.eclipse.swt.widgets.Display

Feature: Code completion

Scenario: Sucessfully using code completion
	extension BackendHelper b = new BackendHelper
	String modelFile
	List<String> proposals
	ContentAssistProcessor proposalProvider
	
	Given a backend for "rtext/test/integration/model/test_metamodel.ect"
		b.startBackendFor(args.first.absolutPath)
	And the model is loaded
		b.connect
	When I invoke the code completion after "  E"
		proposalProvider = ContentAssistProcessor::create([|b.connector]) 
		proposalProvider.assistSessionStarted(_)
		Display::getDefault.syncExec[|
			proposals = proposalProvider.computeCompletionProposals(b.document, b.offsetAfter(args.first), 0).map[displayString.trim]
		]
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
	println(proposals)
	proposals => expectedProposals

Scenario: Code completion for nested elements
	
	Given a backend for "rtext/test/integration/model/test_metamodel.ect"
	And the model is loaded
	When I invoke the code completion after "EClass "
	Then the proposals should be
	'''
		name [name] <EString>
		abstract: <EBoolean>
		interface: <EBoolean>
		eSuperTypes: <EClass>
		instanceClassName: <EString>
	'''

Scenario: Proposal signals backend currently loading
	
	Given a backend for "rtext/test/integration/model/test_metamodel.ect"
	And the backend is busy
		b.busy
	When I invoke the code completion after "EPackage StatemachineMM {\n"
	Then the proposals should be
	'''
		loading model
	'''

Scenario: Proposal signals backend not yet loaded
	
	Given a backend for "rtext/test/integration/model/test_metamodel.ect"
	When I invoke the code completion after "EPackage StatemachineMM {\n"
	Then the proposals should be
	'''
		model not yet loaded
	'''

Scenario: Proposal signals backend failure
	
	Given a backend for "rtext/test/integration/model/test.crashing_backend"
	When I invoke the code completion after "EPackage StatemachineMM {\n"
	Then the proposals should be
	'''
		model not yet loaded
	'''
