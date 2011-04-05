package org.rtext.editor;

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.rtext.backend.Command;
import org.rtext.backend.Connector;


public class ContentAssistProcessor implements IContentAssistProcessor {

	public static Vector<String> keywords = new Vector<String>();
	static {
		keywords.add("house");
		keywords.add("mouse");
		keywords.add("monster");
	}
	
	private Editor editor;
	
	public ContentAssistProcessor(Editor editor) {
		this.editor = editor;		
	}
	
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int offset) {
		
		Vector<CompletionProposal> proposals = new Vector<CompletionProposal>();
		String wordStart = wordStart(viewer, offset);
		int wordStartOffset = offset-wordStart.length();
		
		Connector bc = editor.getBackendConnector();
		if (bc != null) {
			StringTokenizer st = bc.executeCommand(
				new Command("complete", new ContextParser(viewer.getDocument()).getContext(offset)), 1000);
			if (st != null) {
				while (st.hasMoreTokens()) {
					String line = st.nextToken();
					StringTokenizer st2 = new StringTokenizer(line, ";");
					if (st2.hasMoreTokens()) {
						String option = st2.nextToken();
						if (option.startsWith(wordStart)) {
							proposals.add(new CompletionProposal(option, wordStartOffset, wordStart.length(), option.length()));
						}
					}
				}
			}
		}

		ICompletionProposal[] result = new ICompletionProposal[proposals.size()];
		for (int i = 0; i < proposals.size(); i++) {
			result[i] = proposals.elementAt(i); 
		}
	    
        return result;
	}
	
	private String wordStart(ITextViewer viewer, int offset) {
		try {
		  int start = offset - 1;
	      while (((start) >= viewer.getTopIndexStartOffset())
	          && Character.isLetterOrDigit(viewer.getDocument()
	              .getChar(start))) {
	        start--;
	      }
	      start++;
	      return viewer.getDocument().get(start, offset-start);
	    } catch (BadLocationException e) {
	      return "";
	    }
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		char[] result = new char[1];
		result[0] = 'a';
		return result;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// no error
		return null;
	}

}
