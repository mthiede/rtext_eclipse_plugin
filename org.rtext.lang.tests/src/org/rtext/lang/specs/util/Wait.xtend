package org.rtext.lang.specs.util

import org.eclipse.xtend.lib.Property
import org.eclipse.xtext.xbase.lib.Functions$Function0
import org.eclipse.xtext.xbase.lib.Functions

import static org.rtext.lang.specs.util.Clock.*
import static org.rtext.lang.specs.util.Sleeper.*

class WaitConfig {
	@Property var message = "Timeout occurred"
	@Property var duration = 500l
	@Property var pollEvery = 50l
}

class Wait {
	
	def static waitUntil(Functions$Function1<WaitConfig, Boolean> initializer){
		val config = new WaitConfig
		val wait = new Wait(SYSTEM_SLEEPER, SYSTEM_CLOCK)
		val Functions$Function0<Boolean> condition = [
			|initializer.apply(config)
		]
		wait.until(condition, config)
	}

	
	Sleeper sleeper
	Clock clock
	
	new(Sleeper sleeper, Clock clock){
		this.sleeper = sleeper
		this.clock = clock
	}

	def until(Functions$Function0<Boolean> condition, WaitConfig config){
		val start = clock.currentTime
		while(!condition.apply()){
			if(config.timeOut(start)){
				throw new TimeoutError(config.message)
			}
			sleeper.sleep(config.pollEvery)
		}
	}
	
	def timeOut(WaitConfig config, long start){
		clock.currentTime > start + config.duration
	}

} 