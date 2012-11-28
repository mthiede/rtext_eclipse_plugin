package org.rtext.lang.specs.unit.backend

import org.rtext.lang.backend.CommandQueue
import static org.rtext.lang.specs.util.Commands.*
import static org.rtext.lang.backend.CommandQueue$Task.*
describe CommandQueue{
	
	fact "queues commands and callbacks"{
		val task = create(ANY_COMMAND, _)
		subject.add(task)
		subject.size => 1
		subject.take => task
	}
	
	fact "take removes tasks"{
		val task = create(ANY_COMMAND, _)
		subject.add(task)
		subject.take => task
		subject.size => 0
		subject.add(task)
		subject.size => 1
	}
	
	fact "duplicate commands and callbacks are discared"{
		subject.add(create(newCommand("a"), _))
		subject.add(create(newCommand("a"), _))
		subject.size => 1
	}
	
	fact "clear all tasks"{
		subject.add(create(ANY_COMMAND, _))
		subject.clear
		subject.size => 0
		subject.add(create(ANY_COMMAND, _))
		subject.size => 1
	}
	
}