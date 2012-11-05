/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.io.File;

public class ConnectorConfig {
	File configFile;
	String[] patterns;
	String command;
	
	ConnectorConfig(File configFile, String[] patterns, String command) {
		this.configFile = configFile;
		this.patterns = patterns;
		this.command = command;
	}
	public File getConfigFile() {
		return configFile;
	}
	public String[] getPatterns() {
		return patterns;
	}
	public String getCommand() {
		return command;
	}
	public String getIdentifier() {
		return configFile.getAbsolutePath() + command;
	}
}
