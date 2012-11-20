package org.rtext.lang.backend2;

import java.util.List;

public class ProposalsCommand extends Command<Proposals> {

	private List<String> context;
	private int column;

	public ProposalsCommand(List<String> context, int lineOffset) {
		super("content_complete", Proposals.class);
		this.context = context;
		this.column = lineOffset;
	}
	
}
