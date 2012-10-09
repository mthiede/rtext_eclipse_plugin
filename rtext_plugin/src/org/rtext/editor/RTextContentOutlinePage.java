package org.rtext.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.rtext.model.Element;
import org.rtext.model.RTextResource;

public class RTextContentOutlinePage extends ContentOutlinePage implements ModelChangeListener{

	public class RefreshJob extends Job {
		private final RTextResource root;

		public RefreshJob(RTextResource root) {
			super("Refreshing outline view");
			this.root = root;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					getTreeViewer().setInput(root);
				}
			});
			return Status.OK_STATUS;
		}
	}

	private Editor editor;
	private Job refreshJob;
	private ISourceViewer sourceViewer;

	public RTextContentOutlinePage(Editor editor) {
		this.editor = editor;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		final TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new RTextContentProvider());
		viewer.setLabelProvider(new RTextLabelProvider());
		viewer.addSelectionChangedListener(this);
		configureTreeContent();
	}

	private void configureTreeContent() {
		getDocument().addModelListener(this);
		getDocument().readOnly(new IUnitOfWork.Void<RTextResource>() {

			@Override
			public void process(RTextResource state) throws Exception {
				handleModelChange(state);
			}
		});
	}

	private RTextDocument getDocument() {
		return (RTextDocument)sourceViewer.getDocument();
	}

	
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			editor.resetHighlightRange();
		else {
			Element element = (Element) ((IStructuredSelection) selection).getFirstElement();
			int start= element.getOffset();
			int length= element.getLength();
			try {
				editor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				editor.resetHighlightRange();
			}
		}
	}

	@Override
	public void handleModelChange(final RTextResource root) {
		if(refreshJob != null){
			refreshJob.cancel();
		}
		refreshJob = new RefreshJob(root);
		refreshJob.schedule(50);
	}

	public void setSourceViewer(ISourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		getDocument().removeModelListener(this);
	}

}
