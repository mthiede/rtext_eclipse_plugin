/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.io.File;


public class RTextFileFinder {
	public RTextFiles find(File modelFile){
		return new RTextFiles(new RTextFileParser(), modelFile);
	}
}
