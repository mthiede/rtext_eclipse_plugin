package org.rtext.lang.backend2;

import java.io.File;

import org.rtext.lang.backend.RTextFileParser;

public class RTextFileFinder {
	public RTextFiles find(File modelFile){
		return new RTextFiles(new RTextFileParser(), modelFile);
	}
}
