/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.commands;

import com.google.gson.annotations.SerializedName;

public class Response {
	
	private final String type;

	@SerializedName("invocation_id") private int invocationId;
	
	public Response(int invocationId, String type) {
		this.invocationId = invocationId;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public int getInvocationId() {
		return invocationId;
	}
	
}
