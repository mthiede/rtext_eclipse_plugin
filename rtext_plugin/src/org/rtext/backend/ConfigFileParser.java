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

	public static class Config {
		String[] patterns;
		String command;
		Config(String[] patterns, String command) {
			this.patterns = patterns;
			this.command = command;
		}
		public String[] getPatterns() {
			return patterns;
		}
		public String getCommand() {
			return command;
		}
	}
	public static Config[] parse(File configFile) {
		try {
			FileInputStream fis = new FileInputStream(configFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			List<Config> result = new ArrayList<Config>();
			while (line != null) {
				if (line.trim().length() > 1 && line.trim().endsWith(":")) {
					String[] patterns = line.split(",");
					String nextLine = br.readLine();
					if (nextLine != null && nextLine.trim().length() > 0 && !nextLine.trim().endsWith(":")) {
						result.add(new Config(patterns, nextLine));
					}
				}
				line = br.readLine();
			}
			fis.close();
			return (Config[]) result.toArray();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}

}
