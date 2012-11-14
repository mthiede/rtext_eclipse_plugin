package org.rtext.lang.backend2;

public class ProposalsCommand extends Command {

	private String context;
	private int column;

	public ProposalsCommand(String context, int lineOffset) {
		super("content_complete");
		this.context = context;
		this.column = lineOffset;
	}

}
