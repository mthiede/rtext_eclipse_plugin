package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.backend2.ProposalProvider
import java.util.List

Feature: Code completion
	
Background:
	extension BackendHelper b = new BackendHelper
	String modelFile
	
	Given a model file "rtext/test/integration/model/test_metamodel.ect"
		modelFile = args.first
	And a running backend
		b.startBackendFor(modelFile)

Scenario: Sucessfully using code completion
	ProposalProvider proposalProvider
	List<String> proposals 
	Given a proposal provider
		proposalProvider = ProposalProvider::create(b.connector) 
	When I invoke the code completion after "EPackage StatemachineMM {\n"
		proposalProvider.calculateProposals(b.document, b.offsetAfter(args.first), b.proposalAcceptor)
	Then the proposals should be
	'''
		EAnnotation
	    EClass
	    EClassifier
	    EDataType
	    EEnum
	    EGenericType
	    EPackage
	'''
	val expectedProposals = args.first.split("\r?\n").map[trim]
	proposals => expectedProposals