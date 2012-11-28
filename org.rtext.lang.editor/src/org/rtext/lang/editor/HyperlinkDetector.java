/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.editor;

import static org.rtext.lang.backend.CallbackWithTimeout.waitForResponse;
import static org.rtext.lang.util.Workbenches.getActivePage;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.rtext.lang.backend.CallbackWithTimeout;
import org.rtext.lang.backend.Connector;
import org.rtext.lang.backend.ContextParser;
import org.rtext.lang.backend.DocumentContext;
import org.rtext.lang.commands.ReferenceTargets;
import org.rtext.lang.commands.ReferenceTargets.Target;
import org.rtext.lang.commands.ReferenceTargetsCommand;
import org.rtext.lang.workspace.BackendConnectJob;

public class HyperlinkDetector implements IHyperlinkDetector {
	private Connected editor;
	private BackendConnectJob backendConnectJob;
	private CallbackWithTimeout<ReferenceTargets> responseCallback = null;

	public HyperlinkDetector(Connected editor) {
		this.editor = editor;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {
		return detectHyperLinks(textViewer.getDocument(), region);
	}

	public IHyperlink[] detectHyperLinks(IDocument document, IRegion region) {
		if(isWaiting()){
			return null;
		}
		Connector connector = editor.getConnector();
		if (connector == null) {
			return null;
		}
		if(!connector.isConnected()){
			startBackend(connector);
			return createErrorLink("model not yet loaded", region);
		}
		if(connector.isBusy()){
			return createErrorLink("loading model", region);
		}
		try {
			return requestReferenceTargets(connector, document, region);
		} catch (Exception e) {
			return createErrorLink("model not yet loaded", region);
		}
	}

	private boolean isWaiting() {
		return responseCallback != null && responseCallback.isWaiting();
	}

	public void startBackend(final Connector connector) {
		if(backendConnectJob != null && backendConnectJob.getResult() == null){
			return;
		}
		backendConnectJob = new BackendConnectJob(connector);
		backendConnectJob.schedule(100);
	}

	private IHyperlink[] createErrorLink(String message, IRegion region) {
		return new IHyperlink[]{new Hyperlink(getActivePage(), region, "", 0, message), new Hyperlink(getActivePage(), region, "", 0, "")};
	}

	public IHyperlink[] requestReferenceTargets(Connector connector, IDocument document, IRegion region) {
		DocumentContext context = createContext(document, region);
		responseCallback = waitForResponse("Backend is busy", 500);
		connector.execute(new ReferenceTargetsCommand(context), responseCallback);
		try {
			ReferenceTargets referenceTargets = responseCallback.waitForResponse();
			return createHyperlinks(document, region, referenceTargets);
		} catch (TimeoutException e) {
			return createErrorLink(e.getMessage(), region);
		}
	}

	public DocumentContext createContext(IDocument document, IRegion region) {
		return new ContextParser(document).getContext(region.getOffset());
	}
	
	private IHyperlink[] createHyperlinks(IDocument document, IRegion region, ReferenceTargets referenceTargets) {
		Region hyperLinkRegion = createHyperLinkRegion(document, region, referenceTargets);
		if (referenceTargets.getTargets().isEmpty()) {
			return null;
		}
		List<IHyperlink> links = new Vector<IHyperlink>();

		for (Target target : referenceTargets.getTargets()) {
			String filename = target.getFile();
			int line = target.getLine();
			String displayName = target.getDisplay();
			links.add(new Hyperlink(getActivePage(), hyperLinkRegion, filename,	line, displayName));
		}
		return links.toArray(new IHyperlink[0]);
	}

	private Region createHyperLinkRegion(IDocument document, IRegion region, ReferenceTargets referenceTargets) {
		int endColumn  = referenceTargets.getBeginColumn();
		int beginColumn = referenceTargets.getEndColumn();
		try {
			int line = document.getLineOfOffset(region.getOffset());
			int lineOffset = document.getLineOffset(line);
			int offset = lineOffset + beginColumn - 1;
			int length = endColumn - beginColumn + 1;
			return new Region(offset, length);
		} catch (BadLocationException e) {
			return new Region(0, 0);
		}
	}

}
