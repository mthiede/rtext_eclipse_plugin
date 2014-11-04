package org.rtext.lang.specs.util

import org.rtext.lang.backend.ProposalAcceptor

class TestProposalAcceptor implements ProposalAcceptor {

	public var proposals = <String>newArrayList()

	override accept(String proposal) {
		proposals.add(proposal)
	}

}
