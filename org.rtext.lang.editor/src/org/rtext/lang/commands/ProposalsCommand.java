/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import java.util.List;

public class ProposalsCommand extends Command<Proposals> {

	private List<String> context;
	private int column;

	public ProposalsCommand(List<String> context, int lineOffset) {
		super("content_complete", Proposals.class);
		this.context = context;
		this.column = lineOffset;
	}
	
	public List<String> getContext() {
		return context;
	}
	
	public int getColumn() {
		return column;
	}
	
}
