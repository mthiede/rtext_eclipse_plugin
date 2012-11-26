package org.rtext.lang.specs.integration

import org.rtext.lang.editor.HyperlinkDetector
import org.eclipse.jface.text.hyperlink.IHyperlink
import java.util.List
import org.rtext.lang.util.Workbenches

Feature: Finding the element declaration

  Background:
 	HyperlinkDetector hyperLinkDetector
 	List<IHyperlink> hyperlinks
 
  Given a project "test" linked to "rtext/test/integration/model/"
    And a backend for "rtext/test/integration/model/test_metamodel.ect"
    And a hyperlink detector
   	hyperLinkDetector = new HyperlinkDetector([|return b.connector])

  Scenario: Find declaration within same file
  When I get the hyperlinks for "/StatemachineMM/State"
    	hyperlinks = hyperLinkDetector.detectHyperLinks(b.document, b.regionOf(args.first))
  Then it opens an editor for "test_metamodel.ect" 
    	hyperlinks.head.open
    Workbenches::activePage.activeEditor?.title should contain args.first

  Scenario: Hyperlink highlighting
  When I get the hyperlinks for "/StatemachineMM/State"
   Then the hyperlink text is "/StatemachineMM/State"
    	val region = hyperlinks.head.getHyperlinkRegion
    b.document.get(region.offset, region.length) => args.first
