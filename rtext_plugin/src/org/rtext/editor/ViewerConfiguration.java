package org.rtext.editor;

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

public class ViewerConfiguration extends SourceViewerConfiguration {
	private SyntaxScanner scanner;
	private ColorManager colorManager;
	private Editor editor;

	public ViewerConfiguration(Editor editor, ColorManager colorManager) {
		this.editor = editor;
		this.colorManager = colorManager;
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
	    ContentAssistProcessor tagContentAssistProcessor = new ContentAssistProcessor(editor);
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
		return super.getAutoEditStrategies(sourceViewer, contentType);
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