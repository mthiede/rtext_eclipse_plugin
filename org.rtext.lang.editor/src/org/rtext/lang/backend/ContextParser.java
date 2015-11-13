package org.rtext.lang.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.rtext.lang.util.Pair;

public class ContextParser {

	private static final Pattern LINE_BREAK = Pattern.compile(".*[,\\\\]\\s*$");
	private static final Pattern CHILD_LABEL = Pattern.compile("^\\s*\\w+:.*$");
	private static final Pattern OPEN_BRACKET = Pattern.compile(".*\\[.*$");
	private static final Pattern OPEN_BRACKET_ENDLINE = Pattern.compile(".*\\[\\s*$");
	private static final Pattern CLOSE_BRACKET = Pattern.compile("^\\s*\\].*$");
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
		Pair<Pair<List<String>, Integer>, Map<Integer, Integer>> joined = join_lines(lines, pos);
		lines = joined.first.first;
		int new_pos = joined.first.second;
		int non_ignored_lines = 0;
		int array_nesting = 0;
		int block_nesting = 0;
		int last_element_line = 0;
		List<String> result = new ArrayList<String>();
		Collections.reverse(lines);
		boolean prevEmpty = false;
		for (int i = 0; i < lines.size(); i++) {
			String l = lines.get(i);
			String l_rstrip = rstrip(l);
			if (l_rstrip.isEmpty()) {
				if (result.isEmpty() || !prevEmpty) {
					// skip multiple empty lines
					unshift(result, l);
					prevEmpty = true;
				}
				continue;
			} else {
				prevEmpty = false;
			}
			if (i == 0) {
				unshift(result, l);
			} else {
				non_ignored_lines += 1;
				char last = l_rstrip.charAt(l_rstrip.length() - 1);
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
		return new DocumentContext(result, new_pos, joined.second);
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
	public Pair<Pair<List<String>, Integer>, Map<Integer, Integer>> join_lines(List<String> lines, int pos) {
		List<String> outlines = new ArrayList<String>();
		Map<Integer, Integer> lineBreaks = new HashMap<Integer, Integer>();
		Integer lineind = 0;
		while (lines.size() > 0) {
			Integer lbind = lineind++;
			outlines.add(lines.remove(0));
			String last = last(outlines);
			while (lines.size() > 0
					&& (LINE_BREAK.matcher(last).matches() ||
						/* don't join after a child label */
						(!CHILD_LABEL.matcher(last).matches() &&
						 (OPEN_BRACKET_ENDLINE.matcher(last).matches() ||
						  (CLOSE_BRACKET.matcher(first(lines)).matches() &&
						   OPEN_BRACKET.matcher(last).matches()))))) {
				String l = shift(lines);
				if (lines.size() == 0) {
					/*
					 * the prefix might have whitespace on the
					 * right hand side which is relevant for the position
					 */
					pos = last.length() + pos;
				}
				String toAdd = outlines.remove((outlines.size() - 1));
				int len_orig = toAdd.length();
				toAdd = toAdd.replaceAll("\\\\", "");
				/* lineBreaks contains information about which line are after a linebreak (thus preincrement)
				 * and what is their offset in the joined line; this is needed later for link highlighting
				 * again in the broken line
				 * NOTE: size of the backslash must be subtracted */
				lineBreaks.put(++lbind, last.length() - (len_orig - toAdd.length()));
				++lineind;

				outlines.add(toAdd + l);
				last = last(outlines);
			}
		}
		// increase the position by one
		return Pair.of(Pair.of(outlines, pos + 1), lineBreaks);
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
