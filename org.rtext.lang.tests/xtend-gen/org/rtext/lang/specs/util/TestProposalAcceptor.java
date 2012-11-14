package org.rtext.lang.specs.util;

import java.util.ArrayList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.rtext.lang.backend2.ProposalAcceptor;

@SuppressWarnings("all")
public class TestProposalAcceptor implements ProposalAcceptor {
  private ArrayList<String> _proposals = new Function0<ArrayList<String>>() {
    public ArrayList<String> apply() {
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public ArrayList<String> getProposals() {
    return this._proposals;
  }
  
  public void setProposals(final ArrayList<String> proposals) {
    this._proposals = proposals;
  }
  
  public void accept(final String proposal) {
    ArrayList<String> _proposals = this.getProposals();
    _proposals.add(proposal);
  }
}
