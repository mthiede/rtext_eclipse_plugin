package org.rtext.lang.editor;
import org.eclipse.jface.text.DocumentCommand;

public class CommandInfo {
	public String text = "";
	public int offset = -1;
	public int length = 0;
	public int cursorOffset = -1;
	public boolean isChange = false;

	public void modifyCommand(DocumentCommand command) {
		if (!isChange)
			return;
		if (cursorOffset != -1) {
			command.caretOffset = cursorOffset;
			command.shiftsCaret = false;
		}
		if (offset != -1)
			command.offset = offset;
		command.length = length;
		command.text = text;
	}
}