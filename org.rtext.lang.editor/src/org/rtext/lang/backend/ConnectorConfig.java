/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import static java.util.Arrays.asList;
import static org.rtext.lang.util.Files.extension;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConnectorConfig {
	
	private File configFile;
	private Set<String> patterns;
	private String command;
	
	public ConnectorConfig(File configFile, String command, String... patterns) {
		this.configFile = configFile;
		this.patterns = new HashSet<String>(asList(patterns));
		this.command = command;
	}
	
	public File getConfigFile() {
		return configFile;
	}
	public Set<String> getPatterns() {
		return patterns;
	}
	public String getCommand() {
		return command;
	}
	public String getIdentifier() {
		return configFile.getAbsolutePath() + command;
	}
	
	public boolean matches(String fileName){
		return patterns.contains(extension(fileName));
	}

	public File getWorkingDir(){
		return configFile.getParentFile();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result
				+ ((configFile == null) ? 0 : configFile.hashCode());
		result = prime * result
				+ ((patterns == null) ? 0 : patterns.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectorConfig other = (ConnectorConfig) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (configFile == null) {
			if (other.configFile != null)
				return false;
		} else if (!configFile.equals(other.configFile))
			return false;
		if (patterns == null) {
			if (other.patterns != null)
				return false;
		} else if (!patterns.equals(other.patterns))
			return false;
		return true;
	}
	
}
