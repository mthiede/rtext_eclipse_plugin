package org.rtext.lang.workspace;

import static java.util.Collections.emptyList;
import static org.rtext.lang.backend.RTextFile.RTEXT_FILE_NAME;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.PlatformUI;
import org.rtext.lang.RTextPlugin;
import org.rtext.lang.backend.ConnectorConfig;
import org.rtext.lang.backend.ConnectorProvider;
import org.rtext.lang.backend.FileSystemBasedConfigProvider;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.editor.RTextEditor;

public class RTextFileChangeListener implements IResourceChangeListener {

	public static RTextFileChangeListener create(ConnectorProvider connectorProvider) {
		return new RTextFileChangeListener(connectorProvider);
	}

	private ConnectorProvider connectorProvider;
	private Set<String> registeredRTextExtensions;
	private FileSystemBasedConfigProvider configProvider = FileSystemBasedConfigProvider.create();
 
	public RTextFileChangeListener(ConnectorProvider connectorProvider) {
		this.connectorProvider = connectorProvider;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if(event.getDelta() == null) return;
		initRTextExtensions();
		final Map<ConnectorConfig, Boolean> configs = new HashMap<ConnectorConfig, Boolean>();
		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					handleRTextFileChange(delta, configs);
					return true;
				}
			});
		} catch (CoreException e) {
			RTextPlugin.logError("Exception when parsing .rtext file change", e);
		}
		runCleanupJob(configs);
	}

	private void initRTextExtensions() {
		IFileEditorMapping[] fileEditorMappings = PlatformUI.getWorkbench().getEditorRegistry().getFileEditorMappings();
		registeredRTextExtensions = new HashSet<String>(fileEditorMappings.length);
		for (IFileEditorMapping editorMapping : fileEditorMappings) {
			IEditorDescriptor editor = editorMapping.getDefaultEditor();
			if(editor != null && RTextEditor.RTEXT_EDITOR_ID.equals(editor.getId())){
				registeredRTextExtensions.add(editorMapping.getExtension());
			}
		}
	}
	
	private Collection<ConnectorConfig> getConfigurations(IResource resource) {
		Collection<ConnectorConfig> configurations = emptyList();
		if (RTEXT_FILE_NAME.equals(resource.getName())) {
			File configFile = new File(resource.getLocation().toString());
			RTextFile rTextFile = new RTextFileParser().doParse(configFile);
			configurations = rTextFile.getConfigurations();
		} else {
			ConnectorConfig config = configProvider.get(resource.getLocation().toFile().toString());
			if(config != null){
				configurations = Collections.singletonList(config);
			}
		}
		return configurations;
	}

	private void handleRTextFileChange(IResourceDelta delta, Map<ConnectorConfig, Boolean> configs) {
		if (isAdded(delta)) {
			return;
		}
		if (hasNotChanged(delta)){
			return;
		}
		IResource resource = delta.getResource();
		if(resource.getLocation() == null){
			return;
		}
		
		if(isModelFile(resource)){
			scheduleModelLoad(configs, resource);
		}else if(isNotRTextFile(resource)) {
			scheduleRestart(configs, resource);
		}
	}

	private void scheduleRestart(Map<ConnectorConfig, Boolean> configs, IResource resource) {
		addConfig(configs, resource, true);
	}

	private void scheduleModelLoad(Map<ConnectorConfig, Boolean> configs, IResource resource) {
		addConfig(configs, resource, false);
	}

	private void addConfig(Map<ConnectorConfig, Boolean> configs, IResource resource, boolean restart) {
		Collection<ConnectorConfig> connectorConfigs = getConfigurations(resource);
		for (ConnectorConfig config : connectorConfigs) {
			Boolean isRTextFile = configs.get(config);
			if(isRTextFile != null && isRTextFile){
				break;
			}
			configs.put(config, restart);
		}
	}

	private boolean isModelFile(IResource resource) {
		return registeredRTextExtensions.contains(resource.getFileExtension());
	}

	private void runCleanupJob(Map<ConnectorConfig, Boolean> configs) {
		if(configs.isEmpty()){
			return;
		}
		ReloadJob.create(connectorProvider, configs).schedule();
	}

	public boolean hasNotChanged(IResourceDelta delta) {
		int flags = delta.getFlags();
	      if ((flags & IResourceDelta.CONTENT) != 0) {
	         return false;
	      }
	      if ((flags & IResourceDelta.REPLACED) != 0) {
	    	  return false;
	      }
	      if ((flags & IResourceDelta.MARKERS) != 0) {
	          return true;
	      }
		return delta.getKind() != IResourceDelta.CHANGED && delta.getKind() != IResourceDelta.REMOVED;
	}

	public boolean contentHasNotChanged(IResourceDelta delta) {
		return (delta.getFlags() & IResourceDelta.CONTENT) == 0;
	}

	private boolean isNotRTextFile(IResource resource) {
		return resource == null || !RTEXT_FILE_NAME.equals(resource.getName());
	}

	private boolean isAdded(IResourceDelta delta) {
		return delta.getKind() == IResourceDelta.ADDED;
	}

}
