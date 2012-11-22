/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

public class Progress extends Response{
	
	private int percentage;
	
	public Progress(int invocationId, String type, int percentage) {
		super(invocationId, type);
		this.percentage = percentage;
	}
	
	public int getPercentage() {
		return percentage;
	}
}
