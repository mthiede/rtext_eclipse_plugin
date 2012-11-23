package org.rtext.lang.editor;

import java.io.IOException;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.OutputHandler;

public class RTextConsole implements OutputHandler{
	
	private IOConsole console;
	private IOConsoleOutputStream consoleOutputStream;

	public RTextConsole(ConnectorConfig connectorConfig) {
		console = new IOConsole("RText ["+fileName(connectorConfig)+"]", null);
		consoleOutputStream = console.newOutputStream();
		consoleManager().addConsoles(new IConsole[] { console });
	}

	public String fileName(ConnectorConfig connectorConfig) {
		if(connectorConfig != null && connectorConfig.getConfigFile() != null){
			return connectorConfig.getConfigFile().getPath();
		}
		return "----";
	}

	private IConsoleManager consoleManager() {
		return ConsolePlugin.getDefault().getConsoleManager();
	}
	
	public void handle(String string) {
		try {
			consoleOutputStream.write(string + "\n");
		} catch (IOException e) {
			RTextPlugin.logError("Exception while writing to console", e);
		}
	}

}
