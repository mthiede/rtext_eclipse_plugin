package org.rtext.backend;

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
