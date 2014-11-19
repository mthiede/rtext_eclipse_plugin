package org.rtext.lang.backend;

import static java.util.regex.Pattern.MULTILINE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.rtext.lang.util.Pair;

public class ContextParser {

	private static final Pattern OPEN_BRACKET = Pattern.compile(".*\\[\\s*$",
			MULTILINE);
	private static final Pattern CLOSE_BRACKET = Pattern.compile("^\\s*\\].*$",
			MULTILINE);
	private static final Pattern COMMA = Pattern.compile(".*,\\s*$", MULTILINE);
	private IDocument doc;

	public ContextParser(IDocument doc) {
		this.doc = doc;
	}

	public DocumentContext getContext(int offset) {
		try {
			DocumentContext result = extract(getLines(offset),
					getOffsetInLine(offset));
			return result;
		} catch (BadLocationException e) {
			return DocumentContext.INVALID;
		}
	}

	private List<String> getLines(int offset) throws BadLocationException {
		int numberOfLines = doc.getNumberOfLines(0, offset);
		List<String> lines = new ArrayList<String>(numberOfLines);
		for (int i = 0; i < numberOfLines; i++) {
			int lineOffset = doc.getLineOffset(i);
			int lineLength = doc.getLineLength(i);
			String line = doc.get(lineOffset, lineLength);
			String lineDelimiter = doc.getLineDelimiter(i);
			if (lineDelimiter != null) {
				line = line
						.substring(0, line.length() - lineDelimiter.length());
			}
			lines.add(line);
		}
		return lines;
	}

	private int getOffsetInLine(int offset) {
		try {
			int line = doc.getLineOfOffset(offset);
			int lineOffset = doc.getLineOffset(line);
			return offset - lineOffset;
		} catch (BadLocationException e) {
			return 0;
		}
	}

	/*
	 * lines: all lines from the beginning up to and including the current line
	 * pos: position of the cursor in the last lines
	 * 
	 * returns the extracted lines and the new position in the last line
	 */
	private DocumentContext extract(List<String> lines, int pos) {
		lines = filter_lines(lines);
		Pair<List<String>, Integer> joined = join_lines(lines, pos);
		lines = joined.first;
		int new_pos = joined.second;
		int non_ignored_lines = 0;
		int array_nesting = 0;
		int block_nesting = 0;
		int last_element_line = 0;
		List<String> result = new ArrayList<String>();
		Collections.reverse(lines);
		for (int i = 0; i < lines.size(); i++) {
			String l = lines.get(i);
			if (l.isEmpty()) {
				continue;
			}
			if (i == 0) {
				unshift(result, l);
			} else {
				non_ignored_lines += 1;
				char last = l.charAt(l.length() - 1);
				if (last == '{') {
					if (block_nesting > 0) {
						block_nesting -= 1;
					} else if (block_nesting == 0) {
						unshift(result, l);
						last_element_line = non_ignored_lines;
					}
				} else if (last == '}') {
					block_nesting += 1;
				} else if (last == '[') {
					if (array_nesting > 0) {
						array_nesting -= 1;
					} else if (array_nesting == 0) {
						unshift(result, l);
					}
				} else if (last == ']') {
					array_nesting += 1;
				} else if (last == ':') {
					// lable directly above element
					if (non_ignored_lines == last_element_line + 1) {
						unshift(result, l);
					}
				}
			}
		}
		return new DocumentContext(result, new_pos);
	}

	private void unshift(List<String> result, String l) {
		result.add(0, l);
	}

	public List<String> filter_lines(List<String> lines) {
		List<String> filtered = new ArrayList<String>(lines.size());
		for (String line : lines) {
			String content = line.trim();
			if (!content.startsWith("@") && !content.startsWith("#")) {
				filtered.add(line);
			}
		}
		return filtered;
	}

	/*
	 * # when joining two lines, all whitespace after the last character of the
	 * first line is removed # (after , and [); however whitespace at the end of
	 * the last of several joined lines is preserved; # this way the context is
	 * correct even if the cursor is after the end of the last line # (i.e. with
	 * whitespace after the last non-whitespace character)
	 */
	public Pair<List<String>, Integer> join_lines(List<String> lines, int pos) {
		List<String> outlines = new ArrayList<String>();
		while (lines.size() > 0) {
			outlines.add(lines.remove(0));
			String last = last(outlines);
			while (lines.size() > 0
					&& (COMMA.matcher(last).matches()
							|| (OPEN_BRACKET.matcher(last).matches() && last
									.contains(",")) || (CLOSE_BRACKET.matcher(
							first(lines)).matches() && last.contains(",")))) {
				outlines.add(rstrip(outlines.remove((outlines.size() - 1))));
				String l = shift(lines);
				if (lines.size() == 0) {
					/*
					 * strip only left part, the prefix might have whitespace on
					 * the right hand side which is relevant for the position
					 */
					String non_ws_prefix = lstrip(l.substring(0, pos));
					pos = last(outlines).length() + non_ws_prefix.length();
				}
				outlines.add(outlines.remove((outlines.size() - 1)) + lstrip(l));
				last = last(outlines);
			}
		}
		// increase the position by one
		return Pair.of(outlines, pos + 1);
	}

	private String last(List<String> outlines) {
		return outlines.get(outlines.size() - 1);
	}

	private String first(List<String> outlines) {
		return outlines.get(0);
	}

	private <T> T shift(List<T> list) {
		return list.remove(0);
	}

	private String lstrip(String s) {
		return s.replaceAll("^\\s+", "");
	}

	private String rstrip(String s) {
		return s.replaceAll("\\s+$", "");
	}

}
