package org.rtext.lang.specs.unit.backend

import org.eclipse.core.resources.IFile
import org.jnario.runner.CreateWith
import org.mockito.Mock
import org.rtext.lang.backend.ConnectorScope
import org.rtext.lang.commands.LoadModelCallback
import org.rtext.lang.commands.LoadModelCallback$ProblemUpdateJob
import org.rtext.lang.commands.LoadModelCallback$ProblemUpdateJobFactory
import org.rtext.lang.commands.LoadedModel
import org.rtext.lang.commands.LoadedModel$FileProblems
import org.rtext.lang.commands.LoadedModel$Problem
import org.rtext.lang.specs.util.MockInjector
import org.rtext.lang.specs.util.WorkspaceHelper
import org.rtext.lang.util.FileLocator

import static org.jnario.lib.JnarioCollectionLiterals.*
import static org.jnario.lib.Should.*
import static org.mockito.Matchers.*
import static org.mockito.Mockito.*
import org.eclipse.core.resources.IResource
import java.util.List
import java.util.Map

@CreateWith(typeof(MockInjector))
describe LoadModelCallback {
	
	extension WorkspaceHelper = new WorkspaceHelper
	
	@Mock ProblemUpdateJobFactory jobFactory
	@Mock ConnectorScope connectorScope
	@Mock FileLocator fileLocator
	@Mock ProblemUpdateJob updateJob
	@Mock IFile resolvedFile1
	@Mock IFile resolvedFile2
	 
	val problems1 = list(
		new Problem("message1", "error",   1),
		new Problem("message2", "warning", 2)
	)
	
	val problems2 = list(
		new Problem("message3", "error",   1),
		new Problem("message4", "warning", 2)
	)
	
	val loadedModel = new LoadedModel => [
		problems += new FileProblems("myfile1.txt", problems1)
		problems += new FileProblems("myfile2.txt", problems2)
	]
	
	before {
		subject = new LoadModelCallback(jobFactory, fileLocator, connectorScope)
		when(jobFactory.create(anyMap, any)).thenReturn(updateJob)
		when(fileLocator.locate("myfile1.txt")).thenReturn(list(resolvedFile1))
		when(fileLocator.locate("myfile2.txt")).thenReturn(list(resolvedFile2))
		
		when(resolvedFile1.contains(resolvedFile1)).thenReturn(true)
		when(resolvedFile2.contains(resolvedFile2)).thenReturn(true)
		
		when(resolvedFile1.isConflicting(resolvedFile1)).thenReturn(true)
		when(resolvedFile2.isConflicting(resolvedFile2)).thenReturn(true)
	}
	
	fact "creates update job on response with problems per file"{
		subject.commandSent
		subject.handleResponse(loadedModel)
		
		val matcher = matches("problems")[Map<IResource, List<Problem>> it | 
			containsKey(resolvedFile1) &&
			containsKey(resolvedFile2) &&
			containsValue(problems1)  &&
			containsValue(problems2) ]
		verify(jobFactory).create(argThat(matcher) as Map<IResource, List<Problem>>, eq(connectorScope))
	}
	
	fact "schedules update job"{
		subject.commandSent
		subject.handleResponse(loadedModel)
		
		verify(updateJob).schedule
	}

}