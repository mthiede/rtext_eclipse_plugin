package org.rtext.lang.specs.integration

import org.rtext.lang.specs.util.WorkspaceHelper
import org.rtext.lang.specs.util.TestFileLocator
import org.rtext.lang.specs.util.BackendHelper
import org.rtext.lang.editor.LoadModelCallback

Feature: Problem Markers
	
Scenario: Sucessfully using code completion
	extension WorkspaceHelper = new WorkspaceHelper
	extension TestFileLocator = TestFileLocator::getDefault()
	extension BackendHelper = new BackendHelper
	
	Given a project "test" linked to "rtext/test/integration/model/"
		createProject(args.first, args.second.absolutPath)
		
	When I load the model for "test/test_metamodel.ect" 
		startBackendFor(file(args.first).location) 
		executeSynchronousCommand(LoadModelCallback::create)
		
