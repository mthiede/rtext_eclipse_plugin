/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

import static java.lang.Math.max;
import static org.rtext.lang.util.Expectations.expectNotNull;

import java.io.File;

import org.eclipse.core.runtime.Path;

public class Files {
	public static String extension(File file){
		expectNotNull(file);
		String name = file.getName();
		return extension(name);
	}

	public static String extension(String name) {
		expectNotNull(name);
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

	public static boolean isSubdir(String parent, String candidate) {
		Path parentPath = new Path(parent);
		Path candidatePath  = new Path(candidate);
		return parentPath.isPrefixOf(candidatePath);
	}
}
