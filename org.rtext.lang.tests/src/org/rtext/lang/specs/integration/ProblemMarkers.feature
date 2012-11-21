package org.rtext.lang.specs.integration

import org.jnario.lib.StepArguments
import org.rtext.lang.editor.LoadModelCallback
import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.specs.util.TestFileLocator
import org.rtext.lang.specs.util.WorkspaceHelper

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*
import static extension org.rtext.lang.specs.util.Jobs.*

Feature: Problem Markers

Scenario: Valid files have no problem marker
	extension WorkspaceHelper = new WorkspaceHelper
	extension TestFileLocator = TestFileLocator::getDefault()
	extension BackendHelper = new BackendHelper
	
	Given a project "test" linked to "rtext/test/integration/model/"
		createProject(args.first, args.second.absolutPath)
		
	When I load the model for "test/test_metamodel.ect" 
		startBackendFor(file(args.first).location) 
		executeSynchronousCommand(LoadModelCallback::create)
		waitForRTextJobs
	Then "test/test_metamodel.ect"  should have no error markers
		args.first.file.findProblems.empty should be true
	But "test/test_metamodel_with_problems.ect"  should have error markers 
		args.first.file.findProblems.empty should be false
		
