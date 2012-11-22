package org.rtext.lang.editor;

import java.io.IOException;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend2.OutputHandler;

public class RTextConsole implements OutputHandler{
	
	private IOConsole console;
	private IOConsoleOutputStream consoleOutputStream;

	public RTextConsole(String modelFile) {
		console = new IOConsole("RText ["+modelFile+"]", null);
		consoleOutputStream = console.newOutputStream();
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		manager.addConsoles(new IConsole[] { console });
	}
	
	public void handle(String string) {
		try {
			consoleOutputStream.write(string + "\n");
		} catch (IOException e) {
			RTextPlugin.logError("Exception while writing to console", e);
		}
	}

}
