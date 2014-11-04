package org.rtext.lang.specs.util;

import java.util.ArrayList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.rtext.lang.backend.ProposalAcceptor;

@SuppressWarnings("all")
public class TestProposalAcceptor implements ProposalAcceptor {
  public ArrayList<String> proposals = CollectionLiterals.<String>newArrayList();
  
  public void accept(final String proposal) {
    this.proposals.add(proposal);
  }
}
