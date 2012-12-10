package org.rtext.lang.specs.integration

import java.util.List
import org.eclipse.jface.text.hyperlink.IHyperlink
import org.rtext.lang.editor.HyperlinkDetector
import org.rtext.lang.util.Workbenches

import static extension org.jnario.lib.JnarioIterableExtensions.*
import static extension org.jnario.lib.Should.*

Feature: Finding the element declaration

  Background:
 	HyperlinkDetector hyperLinkDetector
 	List<IHyperlink> hyperlinks
 
  Given a project "test" linked to "rtext/test/integration/model/"
    And a backend for "rtext/test/integration/model/test_metamodel.ect"
    And a hyperlink detector
   	hyperLinkDetector = HyperlinkDetector::create(b) as HyperlinkDetector
 
  Scenario: Open an hyperlink
  Given the model is loaded
   When I get the hyperlinks for "/StatemachineMM/State"
    	hyperlinks = hyperLinkDetector.detectHyperLinks(b.document, b.regionOf(args.first))
  Then it opens an editor for "test_metamodel.ect" 
    	hyperlinks.head.open
    Workbenches::activePage.activeEditor?.title should contain args.first
 
  Scenario: Hyperlink highlighting
  Given the model is loaded
    When I get the hyperlinks for "/StatemachineMM/State"
   Then the hyperlink text is "/StatemachineMM/State"
    	val region = hyperlinks.head.getHyperlinkRegion
    b.document.get(region.offset, region.length) => args.first
    
  Scenario: Disconnected backend
  	
  	Given the backend is disconnected
  		b.connector.disconnect
  	When I get the hyperlinks for "/StatemachineMM/State"
   	Then the hyperlink message is "model not yet loaded"
   		hyperlinks.head.hyperlinkText => args.first
  	