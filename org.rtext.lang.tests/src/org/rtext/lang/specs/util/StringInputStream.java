package org.rtext.lang.specs.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class StringInputStream extends ByteArrayInputStream {

	public StringInputStream(String string) {
		super(string.getBytes());
	}
	
	public StringInputStream(String content, String encoding) throws UnsupportedEncodingException {
		super(content.getBytes(encoding));
	}

}