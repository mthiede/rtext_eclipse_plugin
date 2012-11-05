/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionListener;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.rtext.lang.backend.Command;
import org.rtext.lang.backend.Connector;


public class ContentAssistProcessor implements IContentAssistProcessor,	ICompletionListener {

	private RTextEditor editor;
	private List<String> allCompletionOptions;
	
	public ContentAssistProcessor(RTextEditor editor) {
		this.editor = editor;
		this.allCompletionOptions = null;
	}
	
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int offset) {
		Vector<CompletionProposal> proposals = new Vector<CompletionProposal>();
		String wordStart = wordStart(viewer, offset);
		int wordStartOffset = offset-wordStart.length();
		
		loadCompletions(viewer, offset);		
		for (String option : allCompletionOptions) {
			if (filterCompletionOption(option, wordStart)) {
				proposals.add(new CompletionProposal(option, wordStartOffset, wordStart.length(), option.length()));
			}
		}

		ICompletionProposal[] result = new ICompletionProposal[proposals.size()];
		for (int i = 0; i < proposals.size(); i++) {
			result[i] = proposals.elementAt(i); 
		}
	    
        return result;
	}
	
	private boolean filterCompletionOption(String option, String wordStart) {
		if (wordStart.contains("/")) {
			return option.toLowerCase().startsWith(wordStart.toLowerCase());
		}
		else {
			String[] parts = option.split("/");
			String lastPart = parts[parts.length-1];
			return option.toLowerCase().startsWith(wordStart.toLowerCase()) || 
				lastPart.toLowerCase().startsWith(wordStart.toLowerCase());	
		}			
	}
	
	private String wordStart(ITextViewer viewer, int offset) {
		try {
		  int start = offset - 1;
	      while (((start) >= viewer.getTopIndexStartOffset()) && (
	    		Character.isLetterOrDigit(viewer.getDocument().getChar(start)) ||
	        	viewer.getDocument().getChar(start) == '/' ||
	        	viewer.getDocument().getChar(start) == '_'
	          )) {
	        start--;
	      }
	      start++;
	      return viewer.getDocument().get(start, offset-start);
	    } catch (BadLocationException e) {
	      return "";
	    }
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		char[] result = new char[1];
		result[0] = 'a';
		return result;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	public String getErrorMessage() {
		// no error
		return null;
	}

	public void assistSessionEnded(ContentAssistEvent event) {
		allCompletionOptions = null;
	}

	private void loadCompletions(ITextViewer viewer, int offset) {
		if (allCompletionOptions == null) {
			allCompletionOptions = new ArrayList<String>();
			Connector bc = editor.getBackendConnector();
			if (bc != null) {
				List<String> responseLines = bc.executeCommand(
					new Command("complete", new ContextParser(viewer.getDocument()).getContext(offset)), 1000);
				if (responseLines != null) {
					for (String line : responseLines) {
						StringTokenizer st2 = new StringTokenizer(line, ";");
						if (st2.hasMoreTokens()) {
							allCompletionOptions.add(st2.nextToken());
						}
					}
				}
			}			
		}
	}
	
	public void assistSessionStarted(ContentAssistEvent event) {
	}

	public void selectionChanged(ICompletionProposal proposal,
			boolean smartToggle) {
	}

}
