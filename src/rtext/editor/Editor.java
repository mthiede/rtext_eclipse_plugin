package rtext.editor;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;

import rtext.RTextPlugin;
import rtext.backend.Connector;
import rtext.backend.ConnectorManager;

public class Editor extends TextEditor {

	private ColorManager colorManager;

	public Editor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ViewerConfiguration(this, colorManager));
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
		new ProblemUpdater(getInputPath()).updateProblems();
		super.editorSaved();
	}
	
	private IPath getInputPath() {
		IEditorInput input = getEditorInput();
		return ((IPathEditorInput)input).getPath();
	}
	
	public Connector getBackendConnector() {
		return ConnectorManager.getConnector(getInputPath());
	}

}
