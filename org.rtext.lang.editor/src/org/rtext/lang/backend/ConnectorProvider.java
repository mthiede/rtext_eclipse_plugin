/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend;

import java.util.List;


public interface ConnectorProvider {
	Connector get(String modelFilePath);
	void dispose(String rtextFilePath);
	void dispose();
	List<Connector> get(RTextFile rTextFile);
}
