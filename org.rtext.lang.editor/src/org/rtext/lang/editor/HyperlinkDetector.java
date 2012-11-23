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
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ContextParser;
import org.rtext.lang.backend.DocumentContext;
import org.rtext.lang.commands.ReferenceTargets;
import org.rtext.lang.commands.ReferenceTargets.Target;
import org.rtext.lang.commands.ReferenceTargetsCommand;

public class HyperlinkDetector implements IHyperlinkDetector {
	private Connected editor;

	public HyperlinkDetector(Connected editor) {
		this.editor = editor;
	}

	private IHyperlink[] createHyperlinks(Region linkRegion, ReferenceTargets referenceTargets) {
		if (referenceTargets.getTargets().isEmpty()) {
			return null;
		}
		List<IHyperlink> links = new Vector<IHyperlink>();

		for (Target target : referenceTargets.getTargets()) {
			String filename = target.getFile();
			int line = target.getLine();
			String displayName = target.getDisplay();
			links.add(new Hyperlink(getActivePage(), linkRegion, filename, 	line, displayName));
		}
		return links.toArray(new IHyperlink[0]);
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		return detectHyperLinks(textViewer.getDocument(), region);
	}

	public IHyperlink[] detectHyperLinks(IDocument document, IRegion region) {
		Connector connector = editor.getConnector();
		if (connector == null) {
			return null;
		}
		try {
			DocumentContext context = createContext(document, region);
			ReferenceTargets referenceTargets = requestReferenceTargets(connector, context);
			Region linkRegion = createHyperLinkRegion(document, region,	referenceTargets);
			return createHyperlinks(linkRegion, referenceTargets);
		} catch (TimeoutException e) {
			return null;
		}
	}

	public ReferenceTargets requestReferenceTargets(Connector connector, DocumentContext context) throws TimeoutException {
		return connector.execute(new ReferenceTargetsCommand(context));
	}
	public DocumentContext createContext(IDocument document, IRegion region) {
		return new ContextParser(document).getContext(region.getOffset());
	}

	private Region createHyperLinkRegion(IDocument document, IRegion region,
			ReferenceTargets referenceTargets) {
		int endColumn  = referenceTargets.getBeginColumn();
		int beginColumn = referenceTargets.getEndColumn();
		try {
			int line = document.getLineOfOffset(region.getOffset());
			int lineOffset = document.getLineOffset(line);
			int offset = lineOffset + beginColumn;
			int length = endColumn - beginColumn;
			return new Region(offset, length);
		} catch (BadLocationException e) {
			return new Region(0, 0);
		}
	}

}
