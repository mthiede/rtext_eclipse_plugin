package org.rtext.lang.util;

public interface Function<P, R> {
	R apply(P input);
}
