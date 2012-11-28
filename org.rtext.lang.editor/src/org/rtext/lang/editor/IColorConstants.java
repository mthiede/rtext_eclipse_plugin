/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.swt.graphics.RGB;

public interface IColorConstants {
	RGB DEFAULT = new RGB(0, 0, 0);
	RGB LABEL = new RGB(127, 0, 85);
	RGB STRING = new RGB(42, 0, 255);
	RGB IDENTIFIER = new RGB(0, 0, 0);
	RGB NUMBER = new RGB(125, 125, 125);
	RGB COMMAND = new RGB(127, 0, 85);
	RGB COMMENT = new RGB(63, 127, 95);
	RGB ANNOTATION = new RGB(100,100,100);
	RGB GENERICS = new RGB(171, 48, 0);
	RGB REFERENCE = new RGB(200, 0, 0);
}
