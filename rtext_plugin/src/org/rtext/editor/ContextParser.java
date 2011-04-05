package org.rtext.editor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class ContextParser {
	private IDocument doc;
	
	public ContextParser(IDocument doc) {
		this.doc = doc;
	}
	
	public String getContext(int offset) {
		String result = String.valueOf(offsetInLine(offset))+"\n";
		for (String line : parseContext(offset)) {
			result += line + "\n";
		}
		return result;		
	}
	
	private int offsetInLine(int offset) {
		int line;
		try {
			line = doc.getLineOfOffset(offset);
			return offset - doc.getLineOffset(line);
		} catch (BadLocationException e) {
		}
		return 0;
	}
	
	private List<String> parseContext(int offset) {
		LinkedList<String> contextLines = new LinkedList<String>();
		try {
			int startLineno = doc.getLineOfOffset(offset);
			int nonIgnoredLinesCounter = 0;
			int arrayNesting = 0;
			int blockNesting = 0;
			int lastElementsLineCounter = 0;
			contextLines.addFirst(getLine(startLineno));
			lastElementsLineCounter = nonIgnoredLinesCounter;			
			for (int i=startLineno-1; i >= 0; i--) {
				String line = getLine(i).trim();
				if (line.length() == 0 || line.startsWith("#")) {
					// ignore
				}
				else {
					nonIgnoredLinesCounter++;
					if (line.endsWith("{")) {
						if (blockNesting > 0) {
							blockNesting--;
						}
						else if (blockNesting == 0) {
							contextLines.addFirst(line);
							lastElementsLineCounter = nonIgnoredLinesCounter;
						}
					}
					else if (line.endsWith("}")) {
						blockNesting++;
					}
					else if (line.endsWith("[")) {
						if (arrayNesting > 0) {
							arrayNesting--;
						}
						else if (arrayNesting == 0) {
							contextLines.addFirst(line);
						}
					}
					else if (line.endsWith("]")) {
						arrayNesting++;
					}
					else if (line.endsWith(":")) {
						if (nonIgnoredLinesCounter == lastElementsLineCounter+1) {
							contextLines.addFirst(line);
						}
					}
				}
			}			
		} catch (BadLocationException e) {
		}
		return contextLines;
	}
	
	private String getLine(int lineno) {
		try {
			return doc.get(doc.getLineOffset(lineno), doc.getLineLength(lineno));
		} catch (BadLocationException e) {
		}
		return "";
	}
}
