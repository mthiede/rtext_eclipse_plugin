package org.rtext.lang.specs.unit.backend

import org.jnario.runner.CreateWith
import org.mockito.Mock
import org.rtext.lang.backend2.FileSystemBasedConfigProvider
import org.rtext.lang.backend2.RTextFile
import org.rtext.lang.backend2.RTextFiles
import org.rtext.lang.specs.util.MockInjector

@CreateWith(typeof(MockInjector))
describe FileSystemBasedConfigProvider {
	
	@Mock RTextFiles rtextFiles
	@Mock RTextFile rtextFile
	
	fact "finds matching " 

}