package org.rtext.lang.specs.util;

import java.util.ArrayList;
import org.eclipse.core.resources.IContainer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.rtext.lang.specs.util.WorkspaceHelper;

@SuppressWarnings("all")
public class ProjectInitializer implements Procedure1<IContainer> {
  private WorkspaceHelper helper = new WorkspaceHelper();
  
  private final ArrayList<Pair<String, CharSequence>> files = CollectionLiterals.<Pair<String, CharSequence>>newArrayList();
  
  private final ArrayList<String> folders = CollectionLiterals.<String>newArrayList();
  
  private final ArrayList<Pair<String, String>> linkedFolders = CollectionLiterals.<Pair<String, String>>newArrayList();
  
  public void apply(final IContainer p) {
    final Procedure1<Pair<String, String>> _function = new Procedure1<Pair<String, String>>() {
      public void apply(final Pair<String, String> it) {
        String _name = p.getName();
        String _plus = (_name + "/");
        String _key = it.getKey();
        String _plus_1 = (_plus + _key);
        String _value = it.getValue();
        ProjectInitializer.this.helper.createLinkedFolder(_plus_1, _value);
      }
    };
    IterableExtensions.<Pair<String, String>>forEach(this.linkedFolders, _function);
    final Procedure1<String> _function_1 = new Procedure1<String>() {
      public void apply(final String it) {
        String _name = p.getName();
        String _plus = (_name + "/");
        String _plus_1 = (_plus + it);
        ProjectInitializer.this.helper.createFolder(_plus_1);
      }
    };
    IterableExtensions.<String>forEach(this.folders, _function_1);
    final Procedure1<Pair<String, CharSequence>> _function_2 = new Procedure1<Pair<String, CharSequence>>() {
      public void apply(final Pair<String, CharSequence> it) {
        String _name = p.getName();
        String _plus = (_name + "/");
        String _key = it.getKey();
        final String fileName = (_plus + _key);
        CharSequence _value = it.getValue();
        ProjectInitializer.this.helper.writeToFile(_value, fileName);
      }
    };
    IterableExtensions.<Pair<String, CharSequence>>forEach(this.files, _function_2);
  }
  
  public boolean file(final String name, final CharSequence contents) {
    Pair<String, CharSequence> _mappedTo = Pair.<String, CharSequence>of(name, contents);
    return this.files.add(_mappedTo);
  }
  
  public boolean folder(final String name) {
    return this.folders.add(name);
  }
  
  public boolean linkedFolder(final String name, final String path) {
    Pair<String, String> _mappedTo = Pair.<String, String>of(name, path);
    return this.linkedFolders.add(_mappedTo);
  }
}
