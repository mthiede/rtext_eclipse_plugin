/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RTextFile {
	
	public static final String RTEXT_FILE_NAME = ".rtext";
	final Map<String, ConnectorConfig> fileExtensions2Configuration;

	public RTextFile(List<ConnectorConfig> configurations) {
		fileExtensions2Configuration = new HashMap<String, ConnectorConfig>(configurations.size());
		for (ConnectorConfig connectorConfig : configurations) {
			for (String pattern : connectorConfig.getPatterns()) {
				fileExtensions2Configuration.put(pattern, connectorConfig);
			}
		}
	}
	
	public ConnectorConfig getConfiguration(String fileExtension){
		return fileExtensions2Configuration.get(fileExtension);
	}

	public Collection<ConnectorConfig> getConfigurations() {
		return fileExtensions2Configuration.values();
	}
}