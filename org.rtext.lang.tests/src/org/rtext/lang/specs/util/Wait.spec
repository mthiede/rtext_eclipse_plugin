package org.rtext.lang.specs.util

import org.eclipse.xtext.xbase.lib.Functions$Function0
import org.jnario.runner.CreateWith
import org.mockito.Mock

import static org.mockito.Mockito.*

@CreateWith(typeof(MockInjector))
describe Wait {
	
	extension WaitConfig c = new WaitConfig
	@Mock Sleeper sleeper
	@Mock Clock clock
	@Mock Functions$Function0<Boolean> condition 
	
	before subject = new Wait(sleeper, clock)

	fact "waits until condition is true"{
		when(condition.apply).thenReturn(false, false, true)
		waitFor(condition)
		verify(condition, times(3)).apply
	}

	fact "tries every specified polling frequency"{
		pollEvery = 10l
		when(condition.apply).thenReturn(false, false, true)
		waitFor(condition)
		verify(sleeper, times(2)).sleep(pollEvery)			
	}
	
	fact "throws TimeoutError after specified time out"{
		duration = 100l
		when(condition.apply).thenReturn(false)
		when(clock.currentTime).thenReturn(0l, 50l, 100l, 150l)
		waitFor(condition) throws TimeoutError
	}
	
	def waitFor(Functions$Function0<Boolean> condition){
		subject.until(condition, c)
	}
}