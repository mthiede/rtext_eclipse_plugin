/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.rtext.lang.backend.Command;
import org.rtext.lang.backend.Connector;


public class HyperlinkDetector implements IHyperlinkDetector {
	private RTextEditor editor;
	
	public HyperlinkDetector(RTextEditor editor) {
		this.editor = editor;
	}
	
	private Region getLinkRegion(ITextViewer viewer, int currentOffset, String regionDesc) {
		String[] regionParts = regionDesc.split(";");
		int regionStart = Integer.parseInt(regionParts[0]);
		int regionEnd = Integer.parseInt(regionParts[1]);
		if (regionStart >= 0 && regionEnd >= regionStart) {
			IDocument doc = viewer.getDocument();
			try {
				int lineOffset = doc.getLineOffset(doc.getLineOfOffset(currentOffset));
				return new Region(lineOffset+regionStart, regionEnd-regionStart+1); 
			} catch (BadLocationException e) {
			}
		}
		return null;
	}
	
	private IHyperlink[] createHyperlinks(Region linkRegion, List<String> responseLines) {
		List<IHyperlink> links = new Vector<IHyperlink>();

		for (String responseLine : responseLines) {
			String[] parts = responseLine.split(";");
			if (parts.length == 3) {
				String filename = parts[0];
				int line = Integer.parseInt(parts[1]);
				String displayName = parts[2];
				links.add(new Hyperlink(editor.getSite().getPage(), linkRegion, filename, line, displayName));
			}
		}
		if (links.size() > 0) {
			return links.toArray(new IHyperlink[0]);
		}
		else {
			return null;
		}
	}
	
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		String context = new ContextParser(textViewer.getDocument()).getContext(region.getOffset());
		if (context != null) {
			Connector bc = editor.getBackendConnector();
			if (bc != null) {
				List<String> responseLines = bc.executeCommand(new Command("get_reference_targets", context), 1000);
				if (responseLines != null && responseLines.size() > 0) {
					String regionDesc = responseLines.remove(0);
					Region linkRegion = getLinkRegion(textViewer, region.getOffset(), regionDesc);
					if (linkRegion != null) {
						return createHyperlinks(linkRegion, responseLines);						
					}				
				}
			}
		}
		return null;
	}

}
