package org.rtext.lang.specs.unit.backend

import org.jnario.runner.CreateWith
import org.mockito.Mock
import org.rtext.lang.backend.FileSystemBasedConfigProvider
import org.rtext.lang.backend.RTextFile
import org.rtext.lang.backend.RTextFiles
import org.rtext.lang.specs.util.MockInjector

@CreateWith(typeof(MockInjector))
describe FileSystemBasedConfigProvider {
	
	@Mock RTextFiles rtextFiles
	@Mock RTextFile rtextFile
	
	fact "finds matching " 

}