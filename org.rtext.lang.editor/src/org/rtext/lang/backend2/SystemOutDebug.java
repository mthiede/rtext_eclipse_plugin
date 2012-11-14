package org.rtext.lang.backend2;

public class SystemOutDebug implements OutputHandler{

	public void handle(String string) {
		System.out.println(string);
	}

}
