/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.proposals;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
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

public class ContentAssistProcessor implements IContentAssistProcessor {

	private static final String ERROR_REPLACEMENT_STRING = "";
	private static final IContextInformation[] NO_CONTEXTS = {};
	private static final char[] PROPOSAL_ACTIVATION_CHARS = { ':', '/', ',' };

	private Connected connected;
	private ImageHelper imageHelper;

	public ContentAssistProcessor(Connected connected, ImageHelper imageHelper) {
		this.connected = connected;
		this.imageHelper = imageHelper;
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		int topIndexStartOffset = viewer.getTopIndexStartOffset();
		return computeCompletionProposals(document, offset, topIndexStartOffset);
	}

	public ICompletionProposal[] computeCompletionProposals(IDocument document, int offset, int topIndexStartOffset) {
		String wordStart = wordStart(document, topIndexStartOffset, offset);
		String prefix = prefix(document, offset);
		int wordStartOffset = offset - wordStart.length();
		List<Option> options = loadCompletions(document, offset, wordStart);
		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>(options.size());
		for (int i = 0; i < options.size(); i++) {
			Option option = options.get(i);
			String replacementString = option.getInsert();
			if (filterCompletionOption(replacementString, wordStart)) {
				result.add(createProposal(prefix, wordStart, wordStartOffset, option));
			}
		}
		return result.toArray(new ICompletionProposal[result.size()]);
	}

	private String prefix(IDocument document, int offset) {
		String prefix = "";
		try {
			prefix = document.get(offset-1, 1);
		} catch (BadLocationException e) {
		}
		return prefix;
	}

	protected CompletionProposal createProposal(String prefix, String wordStart, int wordStartOffset, Option option) {
		String replacementString = option.getInsert();
		Image image = imageHelper.getImage("element.gif");
		if(wordStart.length() == 0 && (prefix.equals(":") || prefix.equals(","))){
			replacementString = " " + replacementString;
		}
		return new CompletionProposal(replacementString, wordStartOffset,
				wordStart.length(), replacementString.length(), image,
				option.getDisplay(), new ContextInformation(
						option.getDisplay(), ""), null);
	}

	private boolean filterCompletionOption(String option, String wordStart) {
		if (option.length() == 0) {
			return true;
		}
		if(option.startsWith("\"") && !wordStart.startsWith("\"")){
			return true;
		}
		if (wordStart.contains("/")) {
			return option.toLowerCase().startsWith(wordStart.toLowerCase());
		} else {
			wordStart = wordStart.toLowerCase();
			String[] parts = option.split("\\W+");
			for (String part : parts) {
				if(part.toLowerCase().startsWith(wordStart)){
					return true;
				}
			}
			return false;
		}
	}

	private String wordStart(IDocument document, int topIndexStartOffset,
			int offset) {
		try {
			int start = offset - 1;
			while (((start) >= topIndexStartOffset)
					&& (Character.isLetterOrDigit(document.getChar(start))
							|| document.getChar(start) == '/' 
							|| document.getChar(start) == '_'
							|| document.getChar(start) == '"')) {
				start--;
			}
			start++;
			return document.get(start, offset - start);
		} catch (BadLocationException e) {
			return ERROR_REPLACEMENT_STRING;
		}
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	public String getErrorMessage() {
		// no error
		return null;
	}

	private List<Option> loadCompletions(IDocument document, int offset, String wordStart) {
		Connector connector = getConnector();
		if (connector == null) {
			return null;
		}
		Proposals proposals;
		if (!connector.isConnected()) {
			new BackendConnectJob(getConnector()).schedule(100);
			proposals = errorProposal("model not yet loaded", wordStart);
		} else if (connector.isBusy()) {
			proposals = errorProposal("loading model", wordStart);
		} else {
			try {
				proposals = connector.execute(new ProposalsCommand(createContext(document, offset)));
			} catch (Exception e) {
				proposals = errorProposal("Backend not yet available", wordStart);
			}
		}

		return proposals.getOptions();
	}

	public Proposals errorProposal(String message, String wordStart) {
		Option[] options = { new Option(wordStart, message) };
		Proposals errorProposal = new Proposals(-1, wordStart, asList(options));
		return errorProposal;
	}

	public DocumentContext createContext(IDocument document, int offset) {
		ContextParser contextParser = new ContextParser(document);
		return contextParser.getContext(offset);
	}

	private Connector getConnector() {
		return connected.getConnector();
	}


	public static ContentAssistProcessor create(Connected connected) {
		return new ContentAssistProcessor(connected, new PluginImageHelper());
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return NO_CONTEXTS;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return PROPOSAL_ACTIVATION_CHARS;
	}
}
