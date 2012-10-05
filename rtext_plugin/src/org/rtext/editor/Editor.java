package org.rtext.editor;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.rtext.RTextPlugin;
import org.rtext.backend.Connector;
import org.rtext.backend.ConnectorManager;


public class Editor extends TextEditor {

	private ColorManager colorManager;
	private RTextContentOutlinePage myOutlinePage;

	public Editor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ViewerConfiguration(this, colorManager));
		setDocumentProvider(new RTextDocumentProvider());
	}
	
	public void createPartControl(Composite parent) {
		IContextService cs = (IContextService)getSite().getService(IContextService.class);
		cs.activateContext("org.rtext.EditorContext");
		IHandlerService hs = (IHandlerService)getSite().getService(IHandlerService.class);
		hs.activateHandler("org.rtext.OpenElementCommand", new OpenElementHandler(this));
		super.createPartControl(parent);
	}


	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	
	protected void createActions() {
		super.createActions();
		
	    ResourceBundle bundle = RTextPlugin.getDefault().getResourceBundle();
	    IAction a = new TextOperationAction(bundle,
	        "ContentAssistProposal.", this,
	        ISourceViewer.CONTENTASSIST_PROPOSALS);

	    a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
	    setAction("ContentAssistProposal", a);
	}
	
	protected void editorSaved() {
		super.editorSaved();
		new ProblemUpdater(ConnectorManager.getConnector(getInputPath()), getStatusLineManager()).schedule();
	}
	
	private IPath getInputPath() {
		IEditorInput input = getEditorInput();
		return ((IPathEditorInput)input).getPath();
	}
	
	public Connector getBackendConnector() {
		return ConnectorManager.getConnector(getInputPath());
	}
	
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (myOutlinePage == null) {
				myOutlinePage = new RTextContentOutlinePage(this);
				myOutlinePage.setSourceViewer(getSourceViewer());
			}
			return myOutlinePage;
		}
		return super.getAdapter(required);
	}
	
}
