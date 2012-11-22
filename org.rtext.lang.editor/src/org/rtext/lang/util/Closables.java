package org.rtext.lang.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class Closables {

	public static void closeQuietly(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (IOException e) {
		}
	}

	public static void closeQuietly(Socket closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (IOException e) {
		}
	}

}
