/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.List;

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.hyperlink.MultipleHyperlinkPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.rtext.lang.document.RTextReconcilingStrategy;
import org.rtext.lang.proposals.ContentAssistProcessor;

public class ViewerConfiguration extends SourceViewerConfiguration {
	private SyntaxScanner scanner;
	private ColorManager colorManager;
	private RTextEditor editor;
	private EditStrategyProvider editStrategyProvider;

	public ViewerConfiguration(RTextEditor editor, ColorManager colorManager) {
		this(editor, colorManager, new EditStrategyProvider());
	}
	
	public ViewerConfiguration(RTextEditor editor, ColorManager colorManager, EditStrategyProvider editStrategyProvider) {
		this.editor = editor;
		this.colorManager = colorManager;
		this.editStrategyProvider = editStrategyProvider;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
	}

	protected SyntaxScanner getSyntaxScanner() {
		if (scanner == null) {
			scanner = new SyntaxScanner(colorManager);
		}
		return scanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getSyntaxScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}
	
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
	{
	    ContentAssistant assistant = new ContentAssistant();
	    ContentAssistProcessor tagContentAssistProcessor = ContentAssistProcessor.create(editor);
	    assistant.addCompletionListener(tagContentAssistProcessor);
	    assistant.setContentAssistProcessor(tagContentAssistProcessor,
	    		IDocument.DEFAULT_CONTENT_TYPE);
	    assistant.enableAutoActivation(true);
	    assistant.setAutoActivationDelay(500);
	    assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	    return assistant;
	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		return new MonoReconciler(new RTextReconcilingStrategy(), false);
	}

	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		List<IAutoEditStrategy> strategies = editStrategyProvider.getStrategies(sourceViewer, contentType);
		return strategies.toArray(new IAutoEditStrategy[strategies.size()]);
	}
	
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return new IHyperlinkDetector[] { new HyperlinkDetector(editor) };
	}

	public IHyperlinkPresenter getHyperlinkPresenter(ISourceViewer sourceViewer) {
		return new MultipleHyperlinkPresenter(new RGB(0, 0, 255));
	}
	
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new ProblemMarkerHover();
	}
	
}