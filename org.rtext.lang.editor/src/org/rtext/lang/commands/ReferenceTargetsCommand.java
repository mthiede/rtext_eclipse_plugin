package org.rtext.lang.commands;

import org.rtext.lang.backend.DocumentContext;

public class ReferenceTargetsCommand extends CommandWithContext<ReferenceTargets> {

	public ReferenceTargetsCommand(DocumentContext documentContext) {
		super("link_targets", ReferenceTargets.class, documentContext);
	}

}
