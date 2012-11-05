package org.rtext.lang.specs.util

import java.lang.AssertionError

class TimeoutError extends AssertionError {
	new(String message){
		super(message)
	}
}