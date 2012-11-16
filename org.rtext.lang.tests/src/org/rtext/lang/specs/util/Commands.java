package org.rtext.lang.specs.util;

import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.CommandSerializer;
import org.rtext.lang.backend2.Response;

public class Commands {
	
	public static Command<Response> ANY_COMMAND = new Command<Response>(111, "request", "load_model");
	public static Command<Response> OTHER_COMMAND = new Command<Response>(112, "request", "load_model");
	public static String ANY_COMMAND_SERIALIZED = new CommandSerializer().serialize(ANY_COMMAND);

}
