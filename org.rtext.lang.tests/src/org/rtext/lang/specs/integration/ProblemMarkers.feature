package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.specs.util.WorkspaceHelper

Feature: Problem Markers

Scenario: Valid files have no problem marker
	extension WorkspaceHelper = new WorkspaceHelper
	extension BackendHelper b = new BackendHelper
	
	Given a project "test" linked to "rtext/test/integration/model/"
		createProject(args.first, args.second.absolutPath)
		
	When I load the model for "test/test_metamodel_ok.ect2" 
		val config = b.startBackendFor(file(args.first).location) 
		b.loadModel
	Then "test/test_metamodel_ok.ect2"  should have no error markers
		args.first.file.findProblems.empty should be true
	But "test/test_metamodel_error.ect2"  should have error markers 
		args.first.file.findProblems.empty should be false
