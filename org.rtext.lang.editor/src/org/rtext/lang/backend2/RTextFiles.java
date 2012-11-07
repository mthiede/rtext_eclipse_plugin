package org.rtext.lang.backend2;

import java.io.File;
import java.util.Iterator;

import org.rtext.lang.backend.RTextFileParser;

public class RTextFiles implements Iterable<RTextFile>{

	private final class RTextFileIterator implements Iterator<RTextFile> {
		private File currentFile;

		public RTextFileIterator(File modelFile) {
			updateCurrentFile(modelFile);
		}

		private void updateCurrentFile(File modelFile) {
			this.currentFile = rtextFileInSameFolder(modelFile);
		}

		public boolean hasNext() {
			return currentFile != null && currentFile.exists();
		}

		public RTextFile next() {
			RTextFile next = parser.doParse(currentFile);
			updateCurrentFile(currentFile.getParentFile());
			return next;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		private File rtextFileInSameFolder(File file) {
			if(file == null){
				return null;
			}
			String parent = file.getParent();
			return new File(parent + File.separator + ".rtext");
		}
	}

	private final RTextFileParser parser;
	private File modelFile;

	public RTextFiles(RTextFileParser parser, File modelFile) {
		this.parser = parser;
		this.modelFile = modelFile;
	}

	public Iterator<RTextFile> iterator() {
		return new RTextFileIterator(modelFile);
	}
}
