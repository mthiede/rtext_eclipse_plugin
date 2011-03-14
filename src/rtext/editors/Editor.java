package rtext.editors;

import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;

import rtext.RTextPlugin;

public class Editor extends TextEditor {

	private ColorManager colorManager;

	public Editor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ViewerConfiguration(colorManager));
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

}
