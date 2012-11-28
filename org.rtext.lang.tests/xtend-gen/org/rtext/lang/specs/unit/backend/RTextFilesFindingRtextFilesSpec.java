package org.rtext.lang.specs.unit.backend;

import java.io.File;

import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.RTextFile;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Finding .rtext files")
public class RTextFilesFindingRtextFilesSpec extends RTextFilesSpec {
  @Test
  @Named("in the same folder")
  @Order(3)
  public void _inTheSameFolder() throws Exception {
    File _newRTextFile = this.newRTextFile(this.currentFolder);
    this.currentConfig = _newRTextFile;
    RTextFile _doParse = this.parser.doParse(this.currentConfig);
    OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
    _when.thenReturn(this.currentRTextFile);
    RTextFile _first = JnarioIterableExtensions.<RTextFile>first(this.fileFinder);
    boolean _doubleArrow = Should.operator_doubleArrow(_first, this.currentRTextFile);
    Assert.assertTrue("\nExpected fileFinder.first => currentRTextFile but"
     + "\n     fileFinder.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     fileFinder is " + new StringDescription().appendValue(this.fileFinder).toString()
     + "\n     currentRTextFile is " + new StringDescription().appendValue(this.currentRTextFile).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("in parent folder")
  @Order(4)
  public void _inParentFolder() throws Exception {
    File _newRTextFile = this.newRTextFile(this.parentFolder);
    this.parentConfig = _newRTextFile;
    RTextFile _doParse = this.parser.doParse(this.parentConfig);
    OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
    _when.thenReturn(this.parentRTextFile);
    RTextFile _first = JnarioIterableExtensions.<RTextFile>first(this.fileFinder);
    boolean _doubleArrow = Should.operator_doubleArrow(_first, this.parentRTextFile);
    Assert.assertTrue("\nExpected fileFinder.first => parentRTextFile but"
     + "\n     fileFinder.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     fileFinder is " + new StringDescription().appendValue(this.fileFinder).toString()
     + "\n     parentRTextFile is " + new StringDescription().appendValue(this.parentRTextFile).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("in root folder")
  @Order(5)
  public void _inRootFolder() throws Exception {
    File _newRTextFile = this.newRTextFile(this.rootFolder);
    this.rootConfig = _newRTextFile;
    RTextFile _doParse = this.parser.doParse(this.rootConfig);
    OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
    _when.thenReturn(this.rootRTextFile);
    RTextFile _first = JnarioIterableExtensions.<RTextFile>first(this.fileFinder);
    boolean _doubleArrow = Should.operator_doubleArrow(_first, this.rootRTextFile);
    Assert.assertTrue("\nExpected fileFinder.first => rootRTextFile but"
     + "\n     fileFinder.first is " + new StringDescription().appendValue(_first).toString()
     + "\n     fileFinder is " + new StringDescription().appendValue(this.fileFinder).toString()
     + "\n     rootRTextFile is " + new StringDescription().appendValue(this.rootRTextFile).toString() + "\n", _doubleArrow);
    
  }
}
