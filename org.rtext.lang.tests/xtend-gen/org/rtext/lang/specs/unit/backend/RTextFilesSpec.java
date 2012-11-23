package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.Should;
import org.jnario.runner.Contains;
import org.jnario.runner.CreateWith;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Extension;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.backend.RTextFileFinder;
import org.rtext.lang.backend.RTextFileParser;
import org.rtext.lang.backend.RTextFiles;
import org.rtext.lang.specs.unit.backend.RTextFilesFindingRtextFilesSpec;
import org.rtext.lang.specs.util.Files;
import org.rtext.lang.specs.util.MockInjector;

@Contains(RTextFilesFindingRtextFilesSpec.class)
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("RTextFiles")
@CreateWith(value = MockInjector.class)
public class RTextFilesSpec {
  File modelFile;
  
  File currentConfig;
  
  File currentFolder;
  
  @Mock
  RTextFile currentRTextFile;
  
  File parentConfig;
  
  File parentFolder;
  
  @Mock
  RTextFile parentRTextFile;
  
  File rootConfig;
  
  File rootFolder;
  
  @Mock
  RTextFile rootRTextFile;
  
  @Rule
  @Extension
  public TemporaryFolder tempFolder = new Function0<TemporaryFolder>() {
    public TemporaryFolder apply() {
      TemporaryFolder _temporaryFolder = new TemporaryFolder();
      return _temporaryFolder;
    }
  }.apply();
  
  @Mock
  RTextFileParser parser;
  
  RTextFiles fileFinder;
  
  @Before
  public void _createFilesystem() throws Exception {
    File _newFolder = this.tempFolder.newFolder("root");
    this.rootFolder = _newFolder;
    File _newRTextFile = this.newRTextFile(this.rootFolder);
    this.rootConfig = _newRTextFile;
    File _newFolder_1 = this.tempFolder.newFolder("root/parent");
    this.parentFolder = _newFolder_1;
    File _newRTextFile_1 = this.newRTextFile(this.parentFolder);
    this.parentConfig = _newRTextFile_1;
    File _newFolder_2 = this.tempFolder.newFolder("root/parent/current");
    this.currentFolder = _newFolder_2;
    File _newRTextFile_2 = this.newRTextFile(this.currentFolder);
    this.currentConfig = _newRTextFile_2;
    File _newFile = Files.newFile(this.currentFolder, "input.txt");
    this.modelFile = _newFile;
    final Procedure1<RTextFileParser> _function = new Procedure1<RTextFileParser>() {
        public void apply(final RTextFileParser it) {
          RTextFile _doParse = it.doParse(RTextFilesSpec.this.currentConfig);
          OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
          _when.thenReturn(RTextFilesSpec.this.currentRTextFile);
          RTextFile _doParse_1 = it.doParse(RTextFilesSpec.this.parentConfig);
          OngoingStubbing<RTextFile> _when_1 = Mockito.<RTextFile>when(_doParse_1);
          _when_1.thenReturn(RTextFilesSpec.this.parentRTextFile);
          RTextFile _doParse_2 = it.doParse(RTextFilesSpec.this.rootConfig);
          OngoingStubbing<RTextFile> _when_2 = Mockito.<RTextFile>when(_doParse_2);
          _when_2.thenReturn(RTextFilesSpec.this.rootRTextFile);
        }
      };
    ObjectExtensions.<RTextFileParser>operator_doubleArrow(
      this.parser, _function);
    RTextFiles _rTextFiles = new RTextFiles(this.parser, this.modelFile);
    this.fileFinder = _rTextFiles;
  }
  
  @Test
  @Named("Ignores null")
  @Order(1)
  public void _ignoresNull() throws Exception {
    RTextFiles _rtextFiles = this.rtextFiles(null);
    int _size = IterableExtensions.size(_rtextFiles);
    boolean _doubleArrow = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected rtextFiles(null).size => 0 but"
     + "\n     rtextFiles(null).size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     rtextFiles(null) is " + new StringDescription().appendValue(_rtextFiles).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("Ignores not existing files")
  @Order(2)
  public void _ignoresNotExistingFiles() throws Exception {
    File _file = new File("not existing");
    RTextFiles _rtextFiles = this.rtextFiles(_file);
    int _size = IterableExtensions.size(_rtextFiles);
    boolean _doubleArrow = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(0));
    Assert.assertTrue("\nExpected rtextFiles(new File(\"not existing\")).size => 0 but"
     + "\n     rtextFiles(new File(\"not existing\")).size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     rtextFiles(new File(\"not existing\")) is " + new StringDescription().appendValue(_rtextFiles).toString()
     + "\n     new File(\"not existing\") is " + new StringDescription().appendValue(_file).toString() + "\n", _doubleArrow);
    
  }
  
  public RTextFiles rtextFiles(final File file) {
    RTextFileFinder _rTextFileFinder = new RTextFileFinder();
    RTextFiles _find = _rTextFileFinder.find(file);
    return _find;
  }
  
  public File newRTextFile(final File folder) {
    File _newFile = Files.newFile(folder, ".rtext");
    return _newFile;
  }
}
