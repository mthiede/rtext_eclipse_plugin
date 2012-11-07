package org.rtext.lang.util;

import static java.lang.Math.max;
import static org.rtext.lang.util.Expectations.expectNotNull;

import java.io.File;

public class Files {
	public static String extension(File file){
		expectNotNull(file);
		String name = file.getName();
		return extension(name);
	}

	private static String extension(String name) {
		int dotIndex = name.lastIndexOf('.');
		if(dotIndex >= 0){
			return "*" + name.substring(dotIndex);
		}
		int lastSeparator = indexOfLastSeparator(name);
		if(lastSeparator < 0){
			return name;
		}
		return name.substring(lastSeparator+1);
	}

	private static int indexOfLastSeparator(String name) {
		return max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
	}
}
