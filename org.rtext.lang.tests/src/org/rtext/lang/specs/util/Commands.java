package org.rtext.lang.specs.util;

import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.CommandSerializer;
import org.rtext.lang.commands.Response;

public class Commands {
	
	public static Command<Response> newCommand(String command) {
		return new Command<Response>(command, Response.class);
	}

	public static Command<Response> ANY_COMMAND = newCommand("any");
	public static Command<Response> OTHER_COMMAND = newCommand("other");
	
	public static String ANY_COMMAND_SERIALIZED = new CommandSerializer().serialize(ANY_COMMAND);

}
