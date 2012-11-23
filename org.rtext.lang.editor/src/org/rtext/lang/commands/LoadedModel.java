/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LoadedModel extends Response {

	public static class Problem {
		String message;
		String severity;
		int line;
		
		public Problem(String message, String severity, int line) {
			super();
			this.message = message;
			this.severity = severity;
			this.line = line;
		}

		public int getLine() {
			return line;
		}

		public String getMessage() {
			return message;
		}

		public String getSeverity() {
			return severity;
		}
	}

	public static class FileProblems {
		String file;
		List<Problem> problems = new ArrayList<Problem>();
		
		public FileProblems(String file, List<Problem> problems) {
			this.file = file;
			this.problems = problems;
		}

		public String getFile() {
			return file;
		}
		
		public List<Problem> getProblems() {
			return problems;
		}
	}

	public LoadedModel() {
		super(0, "response");
	}

	@SerializedName("total_problems")
	private int totalProblems;
	List<FileProblems> problems = new ArrayList<FileProblems>();

	public List<FileProblems> getProblems() {
		return problems;
	}
	
	public int getProblemCount() {
		return totalProblems;
	}
}
