package org.rtext.editor;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.rtext.backend.Command;
import org.rtext.backend.Connector;


public class HyperlinkDetector implements IHyperlinkDetector {
	private Editor editor;
	
	public HyperlinkDetector(Editor editor) {
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
	
	private IHyperlink[] createHyperlinks(Region linkRegion, StringTokenizer st) {
		List<IHyperlink> links = new Vector<IHyperlink>();

		while (st.hasMoreTokens()) {
			String[] parts = st.nextToken().split(";");
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
				StringTokenizer st = bc.executeCommand(new Command("get_reference_targets", context), 1000);
				if (st != null) {
					if (st.hasMoreTokens()) {
						Region linkRegion = getLinkRegion(textViewer, region.getOffset(), st.nextToken());
						if (linkRegion != null) {
							return createHyperlinks(linkRegion, st);						
						}
					}				
				}
			}
		}
		return null;
	}

}
