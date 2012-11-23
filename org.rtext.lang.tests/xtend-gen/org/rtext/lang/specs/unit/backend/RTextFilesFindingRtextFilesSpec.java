package org.rtext.lang.specs.unit.backend;

import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.hamcrest.StringDescription;
import org.jnario.lib.JnarioIterableExtensions;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.backend.RTextFile;
import org.rtext.lang.specs.unit.backend.RTextFilesSpec;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("Finding .rtext files")
public class RTextFilesFindingRtextFilesSpec extends RTextFilesSpec {
  @Test
  @Named("in the same folder")
  @Order(3)
  public void _inTheSameFolder() throws Exception {
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
    RTextFile _second = JnarioIterableExtensions.<RTextFile>second(this.fileFinder);
    boolean _doubleArrow = Should.operator_doubleArrow(_second, this.parentRTextFile);
    Assert.assertTrue("\nExpected fileFinder.second => parentRTextFile but"
     + "\n     fileFinder.second is " + new StringDescription().appendValue(_second).toString()
     + "\n     fileFinder is " + new StringDescription().appendValue(this.fileFinder).toString()
     + "\n     parentRTextFile is " + new StringDescription().appendValue(this.parentRTextFile).toString() + "\n", _doubleArrow);
    
  }
  
  @Test
  @Named("in root folder")
  @Order(5)
  public void _inRootFolder() throws Exception {
    RTextFile _third = JnarioIterableExtensions.<RTextFile>third(this.fileFinder);
    boolean _doubleArrow = Should.operator_doubleArrow(_third, this.rootRTextFile);
    Assert.assertTrue("\nExpected fileFinder.third => rootRTextFile but"
     + "\n     fileFinder.third is " + new StringDescription().appendValue(_third).toString()
     + "\n     fileFinder is " + new StringDescription().appendValue(this.fileFinder).toString()
     + "\n     rootRTextFile is " + new StringDescription().appendValue(this.rootRTextFile).toString() + "\n", _doubleArrow);
    
    int _size = IterableExtensions.size(this.fileFinder);
    boolean _doubleArrow_1 = Should.operator_doubleArrow(Integer.valueOf(_size), Integer.valueOf(3));
    Assert.assertTrue("\nExpected fileFinder.size => 3 but"
     + "\n     fileFinder.size is " + new StringDescription().appendValue(Integer.valueOf(_size)).toString()
     + "\n     fileFinder is " + new StringDescription().appendValue(this.fileFinder).toString() + "\n", _doubleArrow_1);
    
  }
}
