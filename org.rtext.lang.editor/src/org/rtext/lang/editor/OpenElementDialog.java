/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import java.util.Date;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.commands.Callback;
import org.rtext.lang.commands.Elements;
import org.rtext.lang.commands.Elements.Element;
import org.rtext.lang.commands.FindElementsCommand;
import org.rtext.lang.commands.Progress;


public class OpenElementDialog extends SelectionStatusDialog  {
	public class DialogUpdater implements Callback<Elements> {

		public void commandSent() {
		}

		public void handleProgress(Progress progress) {
			
		}

		public void handleResponse(final Elements elements) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					responseReceived(elements);
				}
			});
		}

		public void handleError(String error) {
		}

	}

	private Text pattern;
	private TableViewer list;
	private Label statusLabel;
	private RTextEditor editor;
	private Date requestSentDate;
	private String lastRequestedPattern;
	private Callback<Elements> callback = new DialogUpdater();
	
	public OpenElementDialog(RTextEditor editor) {
		super(editor.getSite().getShell());
		this.editor = editor;
		requestSentDate = null;
		setHelpAvailable(false);
		setTitle("RText Open Element");
	}
	
	protected IDialogSettings getDialogBoundsSettings() {
		IDialogSettings settings = new DialogSettings("root");
		IDialogSettings section = settings.addNewSection("DialogBoundsSettings");
		section.put("DIALOG_HEIGHT", 500);
		section.put("DIALOG_WIDTH", 600);
		return section;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		Composite content = new Composite(dialogArea, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		content.setLayoutData(gd);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		content.setLayout(layout);

		Label patternLabel = new Label(content, SWT.NONE);
		patternLabel.setText("Enter element name prefix or pattern:");
		patternLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pattern = new Text(content, SWT.SINGLE | SWT.BORDER);
		pattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		createListLabels(content);
		
		list = new TableViewer(content, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.VIRTUAL);

		IContentProvider contentProvider = new ArrayContentProvider();
		list.setContentProvider(contentProvider);
		list.setInput(new Element[0]);
		list.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		pattern.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				requestElements();
			}
		});

		pattern.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					if (list.getTable().getItemCount() > 0) {
						list.getTable().setFocus();
					}
				}
			}
		});

		list.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});

		applyDialogFont(content);

		return dialogArea;
	}
	
	private void createListLabels(Composite parent) {
		Composite labels = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		labels.setLayout(layout);
		labels.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label listLabel = new Label(labels, SWT.NONE);
		listLabel.setText("Matching elements:");
		listLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		statusLabel = new Label(labels, SWT.RIGHT);
		statusLabel.setText("");
		statusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void requestElements() {
		// while a request is ongoing, no new request will be made
		// however after a timeout, the request is considered to be lost a new request will be made anyway
		if (requestSentDate == null || (new Date().getTime() - requestSentDate.getTime()) > 10000) {
			Connector bc = editor.getConnector();
			if (bc != null) {
				bc.execute(new FindElementsCommand(lastRequestedPattern), callback);
				requestSentDate = new Date();
				lastRequestedPattern = pattern.getText();
				indicateStartSearch();
			}
		}
	}
	protected void computeResult() {
		setResult(((StructuredSelection) list.getSelection()).toList());	
	}

	public void responseReceived(Elements elements) {
		if (getContents() != null && !getContents().isDisposed()) {
			indicateStopSearch();
			requestSentDate = null;
			list.setInput(elements.getElements());
			if (!(lastRequestedPattern != null && lastRequestedPattern.equals(pattern.getText()))) {
				requestElements();
			}
		}
	}

	protected void handleShellCloseEvent() {
		super.handleShellCloseEvent();
	}

	public void requestTimedOut() {
		if (getContents() != null && !getContents().isDisposed()) {
			indicateStopSearch();
			requestSentDate = null;		
			requestElements();
		}
	}
	
	private void indicateStartSearch() {
		statusLabel.setText("Searching...");
	}
	
	private void indicateStopSearch() {
		statusLabel.setText("");
	}

}
