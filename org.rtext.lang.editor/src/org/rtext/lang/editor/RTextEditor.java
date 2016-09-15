/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.net.MalformedURLException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
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
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorConfigProvider;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.FileSystemBasedConfigProvider;
import org.rtext.lang.document.IRTextDocument;
import org.rtext.lang.document.RTextDocumentProvider;
import org.rtext.lang.document.RTextDocumentUtil;

public class RTextEditor extends TextEditor implements Connected{

	private ColorManager colorManager;
	private RTextContentOutlinePage myOutlinePage;
	private ProjectionSupport projectionSupport;
	private FoldingStructureProvider foldingStructureProvider = new FoldingStructureProvider();
	private final ConnectorProvider connectorProvider;
	private ConnectorConfigProvider configProvider;
	public static final String RTEXT_EDITOR_ID = "org.rtext.lang.Editor";
	
	public RTextEditor() {
		this(RTextPlugin.getDefault().getConnectorProvider(), FileSystemBasedConfigProvider.create());
	}
	
	public RTextEditor(ConnectorProvider connectorProvider, ConnectorConfigProvider configProvider) {
		this.connectorProvider = connectorProvider;
		this.configProvider = configProvider;
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ViewerConfiguration(this, colorManager));
		setDocumentProvider(new RTextDocumentProvider());
	}

	public void createPartControl(Composite parent) {
		IContextService cs = (IContextService) getSite().getService(IContextService.class);
		cs.activateContext("org.rtext.lang.EditorContext");
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
		projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		installFoldingSupport(viewer);
	}
	
	@Override
	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[]{"org.rtext.lang.EditorContext", "org.eclipse.ui.textEditorScope"});
	}
	
	public void dispose() {
		colorManager.dispose();
		uninstallFoldingSupport();
		super.dispose();
	}

	protected void createActions() {
		super.createActions();
		IHandlerService hs = (IHandlerService) getSite().getService(IHandlerService.class);
		hs.activateHandler("org.rtext.lang.OpenElementCommand",	new OpenElementHandler(this));
		ResourceBundle bundle = RTextPlugin.getDefault().getResourceBundle();
		
		Action action = new ContentAssistAction(bundle, "ContentAssistProposal.", this); 
		String id = ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS;
		action.setActionDefinitionId(id);
		setAction("ContentAssistProposal", action); 
		markAsStateDependentAction("ContentAssistProposal", true);
	}

	protected void editorSaved() {
		super.editorSaved();
	}
	
	@Override
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		// Ensure connected and ready to serve requests
		connectorProvider.get(currentConfig()).connect();
	}
	
	private IPath getInputPath() {
		IEditorInput input = getEditorInput();
		if (input instanceof FileStoreEditorInput) {
			FileStoreEditorInput fileStoreEditorInput = (FileStoreEditorInput) input;
			try {
				return new Path(fileStoreEditorInput.getURI().toURL().getFile());
			} catch (MalformedURLException e) {
				throw new UnsupportedOperationException("Could not handle editor input: " + fileStoreEditorInput.getURI(), e);
			}
		}else if (input instanceof IPathEditorInput) {
			IPathEditorInput pathEditorInput = (IPathEditorInput) input;
			return pathEditorInput.getPath();
		}else{
			throw new UnsupportedOperationException("Unknown editor input type: " + input.getClass().getName());
		}
	}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		return viewer;
	}

	public Connector getConnector() {
		return connectorProvider.get(currentConfig());
	}

	private ConnectorConfig currentConfig() {
		return configProvider.get(path());
	}

	private String path() {
		return getInputPath().toOSString();
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
	
	public void outlinePageClosed() {
		if (myOutlinePage != null) {
			myOutlinePage = null;
			resetHighlightRange();
		}
	}
}
