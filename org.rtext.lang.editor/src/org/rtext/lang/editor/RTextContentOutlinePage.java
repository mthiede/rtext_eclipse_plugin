/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.rtext.lang.document.RTextDocument;
import org.rtext.lang.model.Element;
import org.rtext.lang.model.ModelChangeListener;
import org.rtext.lang.model.RTextContentProvider;
import org.rtext.lang.model.RTextResource;
import org.rtext.lang.util.IUnitOfWork;

public class RTextContentOutlinePage extends ContentOutlinePage implements ModelChangeListener{

	public class RefreshJob extends Job {
		private final RTextResource root;

		public RefreshJob(RTextResource root) {
			super("Refreshing outline view");
			this.root = root;
		}

		protected IStatus run(IProgressMonitor monitor) {
			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if(getTreeViewer().getTree().isDisposed()){
						return;
					}
					getTreeViewer().setInput(root);
				}
			});
			return Status.OK_STATUS;
		}
	}

	private RTextEditor editor;
	private Job refreshJob;
	private ISourceViewer sourceViewer;

	public RTextContentOutlinePage(RTextEditor editor) {
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
		RTextDocument document = getDocument();
		if(document == null){
			return;
		}
		document.addModelListener(this);
		document.readOnly(new IUnitOfWork.Void<RTextResource>() {
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
			try {
				Position position = element.getPosition();
				editor.setHighlightRange(position.offset, position.length, true);
			} catch (IllegalArgumentException x) {
				editor.resetHighlightRange();
			}
		}
	}

	public void handleModelChange(final RTextResource root) {
		if(refreshJob != null){
			refreshJob.cancel();
		}
		refreshJob = new RefreshJob(root);
		refreshJob.schedule();
	}

	public void setSourceViewer(ISourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		RTextDocument document = getDocument();
		if(document == null)
		{
			return;
		}
		document.removeModelListener(this);
	}

}
