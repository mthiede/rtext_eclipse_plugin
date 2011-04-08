package org.rtext.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigFileParser {

	public static List<ConnectorConfig> parse(File configFile) {
		try {
			FileInputStream fis = new FileInputStream(configFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			List<ConnectorConfig> result = new ArrayList<ConnectorConfig>();
			while (line != null) {
				if (line.trim().length() > 1 && line.trim().endsWith(":")) {
					String[] patterns = line.substring(0, line.length()-1).split(",");
					String nextLine = br.readLine();
					if (nextLine != null && nextLine.trim().length() > 0 && !nextLine.trim().endsWith(":")) {
						result.add(new ConnectorConfig(configFile, patterns, nextLine));
					}
				}
				line = br.readLine();
			}
			fis.close();
			return result;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}

}
