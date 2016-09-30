package org.rtext.lang.specs.unit.backend;

import org.junit.Test;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.CommandSerializer;
import org.rtext.lang.commands.Response;

import junit.framework.TestCase;

public class CommandSerializerTestCase extends TestCase {

	@Test
	public static void testJson() {
		String command = "load_model";
		String type = "request";
		int invocationId = 42;
		int version = 1;

		CommandSerializer serializer = new CommandSerializer();
		String serialized = serializer.serialize(new Command<Response>(invocationId, type, command, Response.class));
		assertEquals(responseStringFor(type, command, version, invocationId), serialized);
	}
	

	public static String responseStringFor(String type, String command, int version, int invocationId) {
		String text = String.format(
				"{\"type\":\"%s\",\"command\":\"%s\",\"version\":%d,\"invocation_id\":%d}",
				type, command, version, invocationId);
		return text.length() + text;
	}
	
}
