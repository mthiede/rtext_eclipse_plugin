/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class RTextFileParser {
	
	public interface ConnectorConfigAcceptor{
		public void accept(ConnectorConfig config);
	}

	public RTextFile doParse(File configFile) {
		final List<ConnectorConfig> result = new ArrayList<ConnectorConfig>();
		try {
			InputStream fis = new FileInputStream(configFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			while (line != null) {
			if (line.trim().length() > 1 && line.trim().endsWith(":")) {
				String[] patterns = line.substring(0, line.length()-1).split(",");
				String nextLine = br.readLine();
				if (nextLine != null && nextLine.trim().length() > 0 && !nextLine.trim().endsWith(":")) {
					result.add(new ConnectorConfig(configFile, nextLine, patterns));
				}
			}
			line = br.readLine();
		}
			fis.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return new RTextFile(result);
	}
	
	public static List<ConnectorConfig> parse(File configFile) {
		final List<ConnectorConfig> result = new ArrayList<ConnectorConfig>();
		try {
			InputStream fis = new FileInputStream(configFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			while (line != null) {
				if (line.trim().length() > 1 && line.trim().endsWith(":")) {
					String[] patterns = line.substring(0, line.length()-1).split(",");
					String nextLine = br.readLine();
					if (nextLine != null && nextLine.trim().length() > 0 && !nextLine.trim().endsWith(":")) {
						result.add(new ConnectorConfig(configFile, nextLine, patterns));
					}
				}
				line = br.readLine();
			}
			fis.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return result;
	}

}
