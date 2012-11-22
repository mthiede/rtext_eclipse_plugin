/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.backend2;

import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.Response;

public interface Connection {
	
	public void connect(String address, int port);
	public <T extends Response> void sendRequest(Command<T> request, Callback<T> callback);
	public void close();
	public boolean isConnected();

}
