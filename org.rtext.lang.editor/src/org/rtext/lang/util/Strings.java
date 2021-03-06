/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {
	private static final Pattern COMMAND_PATTERN = Pattern
			.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
	
	public static String[] splitCommand(String s) {
		List<String> matchList = new ArrayList<String>();
		Matcher regexMatcher = COMMAND_PATTERN.matcher(s);
		while (regexMatcher.find()) {
			if (regexMatcher.group(1) != null) {
				// Add double-quoted string without the quotes
				matchList.add(regexMatcher.group(1));
			} else if (regexMatcher.group(2) != null) {
				// Add single-quoted string without the quotes
				matchList.add(regexMatcher.group(2));
			} else {
				// Add unquoted word
				matchList.add(regexMatcher.group());
			}
		}
		return matchList.toArray(new String[matchList.size()]);
	}

	public static String removeLeadingWhitespace(String indentationString) {
		int i = 0;
		while (i < indentationString.length()
				&& Character.isWhitespace(indentationString.charAt(i)))
			i++;
		return indentationString.substring(i);
	}

	public static String join(Iterable<?> strings, String separator) {
		StringBuilder result = new StringBuilder();
		Iterator<?> iterator = strings.iterator();
		while (iterator.hasNext()) {
			Object object = (Object) iterator.next();
			if (object == null) {
				object = "null";
			}
			result.append(object.toString());
			if (iterator.hasNext()) {
				result.append(separator);
			}
		}
		return result.toString();
	}

}
