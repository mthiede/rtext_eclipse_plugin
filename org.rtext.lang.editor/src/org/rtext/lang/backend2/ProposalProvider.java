package org.rtext.lang.backend2;

import java.util.concurrent.TimeoutException;

import org.eclipse.jface.text.IDocument;
import org.rtext.lang.editor.ContextParser;

public class ProposalProvider {
	
	private Connector connector;

	public static ProposalProvider create(Connector connector){
		return new ProposalProvider(connector);
	}
	
	public ProposalProvider(Connector connector) {
		this.connector = connector;
	}

	public void calculateProposals(IDocument document, int offset, ProposalAcceptor acceptor){
		try {
			ContextParser contextParser = new ContextParser(document);
			String context = contextParser.getContext(offset);
			Response response = connector.execute(new ProposalsCommand(context, 0));
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
