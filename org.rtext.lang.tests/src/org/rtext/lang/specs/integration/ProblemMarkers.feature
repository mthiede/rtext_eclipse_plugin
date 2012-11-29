package org.rtext.lang.specs.integration

import org.rtext.lang.commands.LoadModelCallback
import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.specs.util.TestFileLocator
import org.rtext.lang.specs.util.WorkspaceHelper

import static org.rtext.lang.specs.util.Jobs.*

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*

Feature: Problem Markers

Scenario: Valid files have no problem marker
	extension WorkspaceHelper = new WorkspaceHelper
	extension TestFileLocator = TestFileLocator::getDefault()
	extension BackendHelper b = new BackendHelper
	
	Given a project "test" linked to "rtext/test/integration/model/"
		createProject(args.first, args.second.absolutPath)
		
	When I load the model for "test/test_metamodel_ok.ect2" 
		val config = startBackendFor(file(args.first).location) 
		executeSynchronousCommand(LoadModelCallback::create(config))
		waitForRTextJobs
	Then "test/test_metamodel_ok.ect2"  should have no error markers
		args.first.file.findProblems.empty should be true
	But "test/test_metamodel_error.ect2"  should have error markers 
		args.first.file.findProblems.empty should be false
