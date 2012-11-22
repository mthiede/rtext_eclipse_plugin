/*******************************************************************************
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/
package org.rtext.lang.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.MessageFormat;

import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.ResourceMarkerAnnotationModel;
import org.rtext.lang.RTextPlugin;

public class RTextDocumentProvider extends FileDocumentProvider {

	protected class URIInfo extends ElementInfo {

		/**
		 * The flag representing the cached state whether the storage is
		 * modifiable.
		 */
		public boolean isModifiable = false;
		/**
		 * The flag representing the cached state whether the storage is
		 * read-only.
		 */
		public boolean isReadOnly = true;
		/** The flag representing the need to update the cached flag. */
		public boolean updateCache = true;

		public URIInfo(IDocument document, IAnnotationModel model) {
			super(document, model);
		}
	}

	protected IDocument createEmptyDocument() {
		return new RTextDocument();
	}

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = null;
		if (isWorkspaceExternalEditorInput(element)) {
			document = createEmptyDocument();
			if (setDocumentContent(document, (IEditorInput) element, Charset
					.defaultCharset().name())) {
				setupDocument(element, document);
			}
		} else {
			document = super.createDocument(element);
		}
		return document;
	}

	protected boolean isWorkspaceExternalEditorInput(Object element) {
		return element instanceof IURIEditorInput
				&& !(element instanceof IFileEditorInput);
	}

	@Override
	protected ElementInfo createElementInfo(Object element)
			throws CoreException {
		ElementInfo info;
		if (isWorkspaceExternalEditorInput(element)) {
			IDocument document = null;
			IStatus status = null;
			try {
				document = createDocument(element);
			} catch (CoreException x) {
				status = x.getStatus();
				document = createEmptyDocument();
			}

			info = new URIInfo(document, createAnnotationModel(element));
			info.fStatus = status;
		} else {
			info = super.createElementInfo(element);
		}
		return info;
	}

	@Override
	protected IAnnotationModel createAnnotationModel(Object element)
			throws CoreException {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) element;
			return new ResourceMarkerAnnotationModel(input.getFile());
		} else if (element instanceof IURIEditorInput) {
			return new AnnotationModel();
		}
		return super.createAnnotationModel(element);
	}

	@Override
	protected boolean setDocumentContent(IDocument document,
			IEditorInput editorInput, String encoding) throws CoreException {
		boolean result;
		if (isWorkspaceExternalEditorInput(editorInput)) {
			java.net.URI uri = ((IURIEditorInput) editorInput).getURI();
			try {
				InputStream contentStream = null;
				try {
					contentStream = uri.toURL().openStream();
					setDocumentContent(document, contentStream, encoding);
				} finally {
					try {
						if (contentStream != null)
							contentStream.close();
					} catch (IOException e1) {
					}
				}
			} catch (IOException ex) {
				String message = (ex.getMessage() != null ? ex.getMessage()
						: ""); //$NON-NLS-1$
				IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
						IStatus.OK, message, ex);
				throw new CoreException(s);
			}
			result = true;
		} else {
			result = super.setDocumentContent(document, editorInput, encoding);
		}
		return result;
	}

	@Override
	public boolean isModifiable(Object element) {
		if (isWorkspaceExternalEditorInput(element)) {
			URIInfo info = (URIInfo) getElementInfo(element);
			if (info != null) {
				if (info.updateCache) {
					try {
						updateCache((IURIEditorInput) element);
					} catch (CoreException x) {
						handleCoreException(x,
								"XtextDocumentProvider.isModifiable");
					}
				}
				return info.isModifiable;
			}
		}
		return super.isModifiable(element);
	}

	@Override
	public boolean isReadOnly(Object element) {
		if (isWorkspaceExternalEditorInput(element)) {
			URIInfo info = (URIInfo) getElementInfo(element);
			if (info != null) {
				if (info.updateCache) {
					try {
						updateCache((IURIEditorInput) element);
					} catch (CoreException x) {
						handleCoreException(x,
								"XtextDocumentProvider.isReadOnly");
					}
				}
				return info.isReadOnly;
			}
		}
		return super.isReadOnly(element);
	}

	protected void updateCache(IURIEditorInput input) throws CoreException {
		URIInfo info = (URIInfo) getElementInfo(input);
		if (info != null) {
			java.net.URI uri = input.getURI();
			if (uri != null) {
				boolean readOnly = false;
				File file = new File(uri);
				if (!file.exists() || !file.canWrite()) {
					readOnly = true;
				}
				info.isReadOnly = readOnly;
				info.isModifiable = !readOnly;
			}
			info.updateCache = false;
		}
	}

	@Override
	public boolean isDeleted(Object element) {
		if (isWorkspaceExternalEditorInput(element)) {
			final IURIEditorInput input = (IURIEditorInput) element;
			boolean result = !input.exists();
			return result;
		}
		if (element instanceof IFileEditorInput) {
			final IFileEditorInput input = (IFileEditorInput) element;

			final IPath path = input.getFile().getLocation();
			if (path == null) {
				// return true;
				return !input.getFile().exists(); // fixed for EFS compatibility
			}
			return !path.toFile().exists();
		}
		return super.isDeleted(element);
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) throws CoreException {
		if (isWorkspaceExternalEditorInput(element)) {
			CharsetEncoder encoder = Charset.defaultCharset().newEncoder();
			encoder.onMalformedInput(CodingErrorAction.REPLACE);
			encoder.onUnmappableCharacter(CodingErrorAction.REPORT);

			OutputStream stream = null;
			try {
				try {
					monitor.beginTask("Saving", 2000);
					byte[] bytes;
					ByteBuffer byteBuffer = encoder.encode(CharBuffer
							.wrap(document.get()));
					if (byteBuffer.hasArray())
						bytes = byteBuffer.array();
					else {
						bytes = new byte[byteBuffer.limit()];
						byteBuffer.get(bytes);
					}
					URL uriAsString = ((IURIEditorInput) element).getURI()
							.toURL();
					stream = new FileOutputStream(uriAsString.getFile());
					stream.write(bytes, 0, byteBuffer.limit());
				} finally {
					monitor.done();
				}
			} catch (CharacterCodingException ex) {
				String message = MessageFormat
						.format("Some characters cannot be mapped using \"{0}\" character encoding.\n"
								+ "Either change the encoding or remove the characters which are not supported by the \"{0}\" character encoding.",
								new Object[] { Charset.defaultCharset().name() });
				IStatus s = new Status(IStatus.ERROR, RTextPlugin.PLUGIN_ID,
						1 /* EditorsUI.CHARSET_MAPPING_FAILED */, message, null);
				throw new CoreException(s);
			} catch (IOException e) {
				String message = "Could not save file.";
				IStatus s = new Status(IStatus.ERROR, RTextPlugin.PLUGIN_ID,
						IResourceStatus.FAILED_WRITE_LOCAL, message, null);
				throw new CoreException(s);
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
			return;
		}
		super.doSaveDocument(monitor, element, document, overwrite);
	}

	@Override
	protected void doUpdateStateCache(Object element) throws CoreException {
		if (isWorkspaceExternalEditorInput(element)) {
			URIInfo info = (URIInfo) getElementInfo(element);
			if (info != null) {
				info.updateCache = true;
				return;
			}
		}
		super.doUpdateStateCache(element);
	}
}
