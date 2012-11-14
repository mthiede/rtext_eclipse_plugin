package org.rtext.lang.specs.util

import org.rtext.lang.backend2.ProposalAcceptor

class TestProposalAcceptor implements ProposalAcceptor {
	
	@Property var proposals = <String>newArrayList()	

	override accept(String proposal) {
		proposals.add(proposal)
	}
	
}