/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import static org.rtext.lang.util.Files.extension;

import java.io.File;
import java.util.Arrays;

public class ConnectorConfig {
	
	File configFile;
	String[] patterns;
	String command;
	
	public ConnectorConfig(File configFile, String command, String... patterns) {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result
				+ ((configFile == null) ? 0 : configFile.hashCode());
		result = prime * result + Arrays.hashCode(patterns);
		return result;
	}
	
	public File getWorkingDir(){
		return configFile.getParentFile();
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
		if (!Arrays.equals(patterns, other.patterns))
			return false;
		return true;
	}

	public boolean matches(String fileName){
		String extension = extension(fileName);
		for (String pattern : patterns) {
			if(pattern.equals(extension)){
				return true;
			}
		}
		return false;
	}
}
