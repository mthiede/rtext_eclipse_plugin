package org.rtext.lang.specs.util;

import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.CommandSerializer;
import org.rtext.lang.commands.Response;

public class Commands {
	
	public static Command<Response> ANY_COMMAND = new Command<Response>(111, "request", "load_model", Response.class);
	public static Command<Response> OTHER_COMMAND = new Command<Response>(112, "request", "load_model", Response.class);
	public static String ANY_COMMAND_SERIALIZED = new CommandSerializer().serialize(ANY_COMMAND);

}
