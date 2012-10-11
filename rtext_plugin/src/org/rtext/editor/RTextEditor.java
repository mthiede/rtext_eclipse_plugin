package org.rtext.editor;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
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

public class RTextEditor extends TextEditor {

	private ColorManager colorManager;
	private RTextContentOutlinePage myOutlinePage;
	private ProjectionSupport projectionSupport;
	private FoldingStructureProvider foldingStructureProvider = new FoldingStructureProvider();
	
	public RTextEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ViewerConfiguration(this, colorManager));
		setDocumentProvider(new RTextDocumentProvider());
	}

	public void createPartControl(Composite parent) {
		IContextService cs = (IContextService) getSite().getService(
				IContextService.class);
		cs.activateContext("org.rtext.EditorContext");
		IHandlerService hs = (IHandlerService) getSite().getService(
				IHandlerService.class);
		hs.activateHandler("org.rtext.OpenElementCommand",
				new OpenElementHandler(this));
		super.createPartControl(parent);
		
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
		
		projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		installFoldingSupport(viewer);
	}
	
	
	public void dispose() {
		colorManager.dispose();
		uninstallFoldingSupport();
		super.dispose();
	}

	protected void createActions() {
		super.createActions();

		ResourceBundle bundle = RTextPlugin.getDefault().getResourceBundle();
		IAction a = new TextOperationAction(bundle, "ContentAssistProposal.",
				this, ISourceViewer.CONTENTASSIST_PROPOSALS);

		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction("ContentAssistProposal", a);
	}

	protected void editorSaved() {
		super.editorSaved();
		new ProblemUpdater(ConnectorManager.getConnector(getInputPath()),
				getStatusLineManager()).schedule();
	}

	private IPath getInputPath() {
		IEditorInput input = getEditorInput();
		return ((IPathEditorInput) input).getPath();
	}

	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		return viewer;
	}

	public Connector getBackendConnector() {
		return ConnectorManager.getConnector(getInputPath());
	}
	
	protected void installFoldingSupport(ProjectionViewer projectionViewer) {
		foldingStructureProvider.install(this, projectionViewer);
		projectionViewer.doOperation(ProjectionViewer.TOGGLE);
	}
	
	protected void uninstallFoldingSupport() {
		if (foldingStructureProvider != null) {
			foldingStructureProvider.uninstall();
			foldingStructureProvider = null;
		}
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
	
	public IRTextDocument getDocument() {
		return RTextDocumentUtil.get(getSourceViewer());
	}
	
	
}
