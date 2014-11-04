package org.rtext.lang.specs.unit.backend;

import java.io.File;
import org.jnario.lib.Assert;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.specs.unit.backend.RTextFilesSpec;

@Named("Finding .rtext files")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class RTextFilesFindingRtextFilesSpec extends RTextFilesSpec {
  @Test
  @Named("in the same folder")
  @Order(1)
  public void _inTheSameFolder() throws Exception {
    File _newRTextFile = this.newRTextFile(this.currentFolder);
    this.currentConfig = _newRTextFile;
    RTextFile _doParse = this.parser.doParse(this.currentConfig);
    OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
    _when.thenReturn(this.currentRTextFile);
    RTextFile _first = JnarioIterableExtensions.<RTextFile>first(this.fileFinder);
    Assert.assertTrue("\nExpected fileFinder.first => currentRTextFile but"
     + "\n     fileFinder.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     fileFinder is " + new org.hamcrest.StringDescription().appendValue(this.fileFinder).toString()
     + "\n     currentRTextFile is " + new org.hamcrest.StringDescription().appendValue(this.currentRTextFile).toString() + "\n", Should.<RTextFile>operator_doubleArrow(_first, this.currentRTextFile));
    
  }
  
  @Test
  @Named("in parent folder")
  @Order(2)
  public void _inParentFolder() throws Exception {
    File _newRTextFile = this.newRTextFile(this.parentFolder);
    this.parentConfig = _newRTextFile;
    RTextFile _doParse = this.parser.doParse(this.parentConfig);
    OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
    _when.thenReturn(this.parentRTextFile);
    RTextFile _first = JnarioIterableExtensions.<RTextFile>first(this.fileFinder);
    Assert.assertTrue("\nExpected fileFinder.first => parentRTextFile but"
     + "\n     fileFinder.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     fileFinder is " + new org.hamcrest.StringDescription().appendValue(this.fileFinder).toString()
     + "\n     parentRTextFile is " + new org.hamcrest.StringDescription().appendValue(this.parentRTextFile).toString() + "\n", Should.<RTextFile>operator_doubleArrow(_first, this.parentRTextFile));
    
  }
  
  @Test
  @Named("in root folder")
  @Order(3)
  public void _inRootFolder() throws Exception {
    File _newRTextFile = this.newRTextFile(this.rootFolder);
    this.rootConfig = _newRTextFile;
    RTextFile _doParse = this.parser.doParse(this.rootConfig);
    OngoingStubbing<RTextFile> _when = Mockito.<RTextFile>when(_doParse);
    _when.thenReturn(this.rootRTextFile);
    RTextFile _first = JnarioIterableExtensions.<RTextFile>first(this.fileFinder);
    Assert.assertTrue("\nExpected fileFinder.first => rootRTextFile but"
     + "\n     fileFinder.first is " + new org.hamcrest.StringDescription().appendValue(_first).toString()
     + "\n     fileFinder is " + new org.hamcrest.StringDescription().appendValue(this.fileFinder).toString()
     + "\n     rootRTextFile is " + new org.hamcrest.StringDescription().appendValue(this.rootRTextFile).toString() + "\n", Should.<RTextFile>operator_doubleArrow(_first, this.rootRTextFile));
    
  }
}
