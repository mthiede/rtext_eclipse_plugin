package org.rtext.lang.editor;

import org.eclipse.jface.text.IDocument;
import org.rtext.lang.backend2.Connector;

public class ProposalProvider {

	private IDocument document;
	private Connector connector;
	private ContextParser contextParser;

	public ProposalProvider(IDocument document, Connector connector,
			ContextParser contextParser) {
				this.document = document;
				this.connector = connector;
				this.contextParser = contextParser;
	}

	public static ProposalProvider create(IDocument document, Connector connector) {
		return new ProposalProvider(document, connector, new ContextParser(document));
	}

}
