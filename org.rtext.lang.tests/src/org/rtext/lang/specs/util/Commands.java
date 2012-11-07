package org.rtext.lang.specs.util;

import org.rtext.lang.backend2.Command;
import org.rtext.lang.backend2.CommandSerializer;

public class Commands {
	
	public static Command ANY_COMMAND = new Command(111, "request", "load_model");
	public static String ANY_COMMAND_SERIALIZED = new CommandSerializer().serialize(ANY_COMMAND);

}
