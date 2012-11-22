/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend2;

import static org.rtext.lang.util.Expectations.expectExists;
import static org.rtext.lang.util.Expectations.expectNotEmpty;
import static org.rtext.lang.util.Files.extension;

import java.io.File;

import org.rtext.lang.backend.ConnectorConfig;

public class FileSystemBasedConfigProvider implements ConnectorConfigProvider {

	public static FileSystemBasedConfigProvider create(){
		return new FileSystemBasedConfigProvider(new RTextFileFinder());
	}

	private RTextFileFinder rTextFileFinder;

	public FileSystemBasedConfigProvider(RTextFileFinder rTextFileFinder) {
		this.rTextFileFinder = rTextFileFinder;
	}

	
	public ConnectorConfig get(String modelFilePath) {
		File modelFile = getFile(modelFilePath);
		RTextFiles rTextFiles = findRTextFiles(modelFile);
		return findMatchingConfig(modelFile, rTextFiles);
	}

	protected File getFile(String modelFilePath) {
		expectNotEmpty(modelFilePath);
		File modelFile = new File(modelFilePath);
		expectExists(modelFile);
		return modelFile;
	}

	protected RTextFiles findRTextFiles(File modelFile) {
		return rTextFileFinder.find(modelFile);
	}

	protected ConnectorConfig findMatchingConfig(File modelFile,
			RTextFiles rTextFiles) {
		for (RTextFile rTextFile : rTextFiles) {
			String fileExtension = extension(modelFile);
			ConnectorConfig config = rTextFile.getConfiguration(fileExtension);
			if(config != null){
				return config;
			}
		}
		return null;
	}

}
