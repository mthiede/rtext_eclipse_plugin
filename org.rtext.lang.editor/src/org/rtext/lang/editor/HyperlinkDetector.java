/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import static org.rtext.lang.util.Workbenches.getActivePage;

import java.util.List;
import java.util.Vector;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.rtext.lang.backend.CommandExecutor;
import org.rtext.lang.backend.CommandExecutor.ExecutionHandler;
import org.rtext.lang.backend.ContextParser;
import org.rtext.lang.backend.DocumentContext;
import org.rtext.lang.commands.Command;
import org.rtext.lang.commands.ReferenceTargets;
import org.rtext.lang.commands.ReferenceTargets.Target;
import org.rtext.lang.commands.ReferenceTargetsCommand;

public class HyperlinkDetector implements IHyperlinkDetector {
	
	public class ReferenceTargetsHandler implements
			ExecutionHandler<ReferenceTargets> {

		private IRegion region;
		private IDocument document;
		private IHyperlink[] result = null;
		private Set<Integer> lineBreaks;

		public ReferenceTargetsHandler(IDocument document, IRegion region, Set<Integer> lineBreaks) {
			this.document = document;
			this.region = region;
			this.lineBreaks = lineBreaks;
		}

		public void handleResult(ReferenceTargets referenceTargets) {
			result = createHyperlinks(document, region, referenceTargets, lineBreaks);
		}

		public void handle(String message) {
			result = createErrorLink(message, region);
		}

	}

	private CommandExecutor commandExecutor;
	private static final Pattern WORD = Pattern.compile("\\w+");

	public HyperlinkDetector(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		return detectHyperLinks(textViewer.getDocument(), region);
	}

	public IHyperlink[] detectHyperLinks(IDocument document, IRegion region) {
		DocumentContext context = createContext(document, region);
		Command<ReferenceTargets> command = new ReferenceTargetsCommand(context);
		ReferenceTargetsHandler executionHandler = new ReferenceTargetsHandler(document, region, context.getLineBreaks());
		commandExecutor.run(command, executionHandler);
		return executionHandler.result;
	}

	private IHyperlink[] createErrorLink(String message, IRegion region) {
		return new IHyperlink[]{new Hyperlink(getActivePage(), region, "", 0, message), new Hyperlink(getActivePage(), region, "", 0, "")};
	}

	public DocumentContext createContext(IDocument document, IRegion region) {
		return new ContextParser(document).getContext(region.getOffset());
	}
	
	private IHyperlink[] createHyperlinks(IDocument document, IRegion region, ReferenceTargets referenceTargets,
			Set<Integer> lineBreaks) {
		Region hyperLinkRegion = createHyperLinkRegion(document, region, referenceTargets, lineBreaks);
		if (referenceTargets.getTargets().isEmpty()) {
			return null;
		}
		List<IHyperlink> links = new Vector<IHyperlink>();

		for (Target target : referenceTargets.getTargets()) {
			String filename = target.getFile();
			int line = target.getLine();
			String displayName = target.getDisplay();
			links.add(new Hyperlink(getActivePage(), hyperLinkRegion, filename,	line, displayName));
		}
		return links.toArray(new IHyperlink[0]);
	}

	private Region createHyperLinkRegion(IDocument document, IRegion region, ReferenceTargets referenceTargets,
			Set<Integer> lineBreaks) {
		if (referenceTargets.getTargets().size() == 0) {
			return new Region(0, 0);
		}
		int endColumn  = referenceTargets.getBeginColumn();
		int beginColumn = referenceTargets.getEndColumn();
		try {
			int line = document.getLineOfOffset(region.getOffset());
			int lineOffset = document.getLineOffset(line);
			int length = endColumn - beginColumn + 1;
			int offset = lineOffset + beginColumn - 1;
			/* current line is after a linebreak, need to substract part from the offset
			 * which belongs to the previous line(s) */
			if (lineBreaks.contains(line)) {
				int lineLength = document.getLineLength(line);
				String s = document.get(lineOffset, lineLength);
				/* find beginning of the link */
				Matcher m = WORD.matcher(s);
				offset -= (beginColumn - 1);
				int prevStart = 0;
				while (m.find() && (region.getOffset() > (offset + length))) {
					/* there is text (labels or other links) earlier in the line before this one,
					 * scroll ahead until we are until we are within the currently selected link */
					offset += (m.start() - prevStart);
					prevStart = m.start();
				}
			}
			return new Region(offset, length);
		} catch (BadLocationException e) {
			return new Region(0, 0);
		}
	}

	public static IHyperlinkDetector create(Connected connected) {
		return new HyperlinkDetector(CommandExecutor.create(connected));
	}

}
