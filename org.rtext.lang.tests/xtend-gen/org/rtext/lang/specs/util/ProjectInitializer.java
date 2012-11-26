package org.rtext.lang.specs.util;

import java.util.ArrayList;
import org.eclipse.core.resources.IContainer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.rtext.lang.specs.util.WorkspaceHelper;

@SuppressWarnings("all")
public class ProjectInitializer implements Procedure1<IContainer> {
  private WorkspaceHelper helper = new Function0<WorkspaceHelper>() {
    public WorkspaceHelper apply() {
      WorkspaceHelper _workspaceHelper = new WorkspaceHelper();
      return _workspaceHelper;
    }
  }.apply();
  
  private final ArrayList<Pair<String,CharSequence>> files = new Function0<ArrayList<Pair<String,CharSequence>>>() {
    public ArrayList<Pair<String,CharSequence>> apply() {
      ArrayList<Pair<String,CharSequence>> _newArrayList = CollectionLiterals.<Pair<String,CharSequence>>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private final ArrayList<String> folders = new Function0<ArrayList<String>>() {
    public ArrayList<String> apply() {
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public void apply(final IContainer p) {
    final Procedure1<String> _function = new Procedure1<String>() {
        public void apply(final String it) {
          String _name = p.getName();
          String _plus = (_name + "/");
          String _plus_1 = (_plus + it);
          ProjectInitializer.this.helper.createFolder(_plus_1);
        }
      };
    IterableExtensions.<String>forEach(this.folders, _function);
    final Procedure1<Pair<String,CharSequence>> _function_1 = new Procedure1<Pair<String,CharSequence>>() {
        public void apply(final Pair<String,CharSequence> it) {
          String _name = p.getName();
          String _plus = (_name + "/");
          String _key = it.getKey();
          final String fileName = (_plus + _key);
          CharSequence _value = it.getValue();
          ProjectInitializer.this.helper.writeToFile(_value, fileName);
        }
      };
    IterableExtensions.<Pair<String,CharSequence>>forEach(this.files, _function_1);
  }
  
  public boolean file(final String name, final CharSequence contents) {
    Pair<String,CharSequence> _mappedTo = Pair.<String, CharSequence>of(name, contents);
    boolean _add = this.files.add(_mappedTo);
    return _add;
  }
  
  public boolean folder(final String name) {
    boolean _add = this.folders.add(name);
    return _add;
  }
}
