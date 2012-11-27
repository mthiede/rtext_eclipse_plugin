/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.proposals;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionListener;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.rtext.lang.backend.BackendException;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ContextParser;
import org.rtext.lang.backend.DocumentContext;
import org.rtext.lang.commands.Proposals;
import org.rtext.lang.commands.Proposals.Option;
import org.rtext.lang.commands.ProposalsCommand;
import org.rtext.lang.editor.Connected;
import org.rtext.lang.editor.PluginImageHelper;
import org.rtext.lang.util.ImageHelper;
import org.rtext.lang.workspace.BackendConnectJob;


public class ContentAssistProcessor implements IContentAssistProcessor,	ICompletionListener {

	private static final String ERROR_REPLACEMENT_STRING = "";
	private Connected connected;
	private Proposals proposals;
	private ImageHelper imageHelper;
	
	public ContentAssistProcessor(Connected connected, ImageHelper imageHelper) {
		this.connected = connected;
		this.imageHelper = imageHelper;
	}
	
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int offset) {
		IDocument document = viewer.getDocument();
		int topIndexStartOffset = viewer.getTopIndexStartOffset();
		return computeCompletionProposals(document, offset, topIndexStartOffset);
	}

	public ICompletionProposal[] computeCompletionProposals(IDocument document,	int offset, int topIndexStartOffset) {
		String wordStart = wordStart(document, topIndexStartOffset, offset);
		int wordStartOffset = offset-wordStart.length();
		List<Option> options = loadCompletions(document, offset);	
		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>(options.size());
		for (int i = 0; i < options.size(); i++) {
			Option option = options.get(i);
			String replacementString = option.getInsert();
			if (filterCompletionOption(replacementString, wordStart)) {
				result.add(createProposal(wordStart, wordStartOffset, option));
			}
		}
        return result.toArray(new ICompletionProposal[result.size()]);
	}

	protected CompletionProposal createProposal(String wordStart,
			int wordStartOffset, Option option) {
		String replacementString = option.getInsert();
		Image image = imageHelper.getImage("element.gif");
		return new CompletionProposal(
				replacementString , 
				wordStartOffset, 
				wordStart.length(), 
				replacementString.length(),
				image,
				option.getDisplay(),
				null,
				null);
	}
	
	private boolean filterCompletionOption(String option, String wordStart) {
		if(option.length() == 0){
			return true;
		}
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
	
	private String wordStart(IDocument document, int topIndexStartOffset, int offset) {
		try {
		  int start = offset - 1;
	      while (((start) >= topIndexStartOffset) && (
	    		Character.isLetterOrDigit(document.getChar(start)) ||
	    		document.getChar(start) == '/' ||
	    				document.getChar(start) == '_'
	          )) {
	        start--;
	      }
	      start++;
	      return document.get(start, offset-start);
	    } catch (BadLocationException e) {
	      return ERROR_REPLACEMENT_STRING;
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
		proposals = null;
	}

	private List<Option> loadCompletions(IDocument document, int offset) {
		if (proposals != null) {
			return emptyList();
		}
		Connector connector = getConnector();
		if (connector  == null) {
			proposals = errorProposal("Could not locate .rtext file");
		}else if (!connector.isConnected()) {
			new BackendConnectJob(getConnector()).schedule();
			proposals = errorProposal("Backend not yet available");
		}else{
			try {
				proposals = connector.execute(new ProposalsCommand(createContext(document, offset)));
			}catch (BackendException e) {
				proposals = errorProposal("Cannot load backend");
			}catch (TimeoutException e) {
				proposals = errorProposal("Cannot load backend");
			}
		}
		return proposals.getOptions();
	}

	public Proposals errorProposal(String message) {
		Option[] options = {new Option(ERROR_REPLACEMENT_STRING, message)};
		Proposals errorProposal = new Proposals(-1, ERROR_REPLACEMENT_STRING, asList(options));
		return errorProposal;
	}

	public DocumentContext createContext(IDocument document, int offset) {
		ContextParser contextParser = new ContextParser(document);
		return contextParser.getContext(offset);
	}
	
	private Connector getConnector(){
		return connected.getConnector();
	}

	public void assistSessionStarted(ContentAssistEvent event) {
	}

	public void selectionChanged(ICompletionProposal proposal, boolean smartToggle) {
	}

	public static ContentAssistProcessor create(Connected connected) {
		return new ContentAssistProcessor(connected, new PluginImageHelper());
	}

}
