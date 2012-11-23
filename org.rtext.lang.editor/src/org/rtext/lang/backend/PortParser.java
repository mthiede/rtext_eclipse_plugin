/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import static java.lang.Integer.parseInt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PortParser implements OutputHandler {
	
	private static final int UNSET = -1;
	
	private Pattern portPattern = Pattern.compile("listening on port (\\d+)");
	private int port = UNSET;

	public void handle(String text) {
		if (text == null) {
			return;
		}
		Matcher matcher = portPattern.matcher(text);
		if (matcher.find()) {
			port = parseInt(matcher.toMatchResult().group(1));
		}
	}
	
	public boolean hasReceivedPort(){
		return port != UNSET;
	}

	public int getPort() {
		return port;
	}

	public void clear() {
		port = UNSET;
	}

}
