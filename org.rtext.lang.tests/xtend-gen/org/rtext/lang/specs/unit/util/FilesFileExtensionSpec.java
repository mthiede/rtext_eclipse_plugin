package org.rtext.lang.specs.unit.util;

import java.io.File;
import java.util.Arrays;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.jnario.lib.Assert;
import org.jnario.lib.Each;
import org.jnario.lib.ExampleTable;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.unit.util.FilesFileExtensionSpecExamples;
import org.rtext.lang.specs.unit.util.FilesSpec;
import org.rtext.lang.util.Files;

@Named("fileExtension")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class FilesFileExtensionSpec extends FilesSpec {
  String nameIsNull = null;
  
  @Test
  @Named("^extension[nameIsNull] throws IllegalArgumentException")
  @Order(1)
  public void _extensionNameIsNullThrowsIllegalArgumentException() throws Exception {
    boolean expectedException = false;
    String message = "";
    try{
      Files.extension(this.nameIsNull);
      message = "Expected " + IllegalArgumentException.class.getName() + " for \n     ^extension(nameIsNull)\n with:"
       + "\n     nameIsNull is " + new org.hamcrest.StringDescription().appendValue(this.nameIsNull).toString();
    }catch(IllegalArgumentException e){
      expectedException = true;
    }
    Assert.assertTrue(message, expectedException);
  }
  
  public ExampleTable<FilesFileExtensionSpecExamples> _initFilesFileExtensionSpecExamples() {
    return ExampleTable.create("examples", 
      Arrays.asList("filename", "ext"), 
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"\"", "\"\""), _initFilesFileExtensionSpecExamplesCell0(), _initFilesFileExtensionSpecExamplesCell1()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"file.txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell2(), _initFilesFileExtensionSpecExamplesCell3()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"file.txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell4(), _initFilesFileExtensionSpecExamplesCell5()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"test.file.txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell6(), _initFilesFileExtensionSpecExamplesCell7()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"txt\"", "\"txt\""), _initFilesFileExtensionSpecExamplesCell8(), _initFilesFileExtensionSpecExamplesCell9()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"/folder/file.txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell10(), _initFilesFileExtensionSpecExamplesCell11()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\".txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell12(), _initFilesFileExtensionSpecExamplesCell13()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"/folder/.txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell14(), _initFilesFileExtensionSpecExamplesCell15()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"\\\\folder\\\\.txt\"", "\"*.txt\""), _initFilesFileExtensionSpecExamplesCell16(), _initFilesFileExtensionSpecExamplesCell17()),
      new FilesFileExtensionSpecExamples(  Arrays.asList("\"\\\\folder\\\\txt\"", "\"txt\""), _initFilesFileExtensionSpecExamplesCell18(), _initFilesFileExtensionSpecExamplesCell19())
    );
  }
  
  protected ExampleTable<FilesFileExtensionSpecExamples> examples = _initFilesFileExtensionSpecExamples();
  
  public String _initFilesFileExtensionSpecExamplesCell0() {
    return "";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell1() {
    return "";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell2() {
    return "file.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell3() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell4() {
    return "file.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell5() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell6() {
    return "test.file.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell7() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell8() {
    return "txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell9() {
    return "txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell10() {
    return "/folder/file.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell11() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell12() {
    return ".txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell13() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell14() {
    return "/folder/.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell15() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell16() {
    return "\\folder\\.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell17() {
    return "*.txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell18() {
    return "\\folder\\txt";
  }
  
  public String _initFilesFileExtensionSpecExamplesCell19() {
    return "txt";
  }
  
  @Test
  @Named("examples.forEach[new File[filename].^extension => ext]")
  @Order(2)
  public void _examplesForEachNewFileFilenameExtensionExt() throws Exception {
    final Procedure1<FilesFileExtensionSpecExamples> _function = new Procedure1<FilesFileExtensionSpecExamples>() {
      public void apply(final FilesFileExtensionSpecExamples it) {
        String _filename = it.getFilename();
        File _file = new File(_filename);
        String _extension = Files.extension(_file);
        String _ext = it.getExt();
        Assert.assertTrue("\nExpected new File(filename).^extension => ext but"
         + "\n     new File(filename).^extension is " + new org.hamcrest.StringDescription().appendValue(_extension).toString()
         + "\n     new File(filename) is " + new org.hamcrest.StringDescription().appendValue(_file).toString()
         + "\n     filename is " + new org.hamcrest.StringDescription().appendValue(_filename).toString()
         + "\n     ext is " + new org.hamcrest.StringDescription().appendValue(_ext).toString() + "\n", Should.<String>operator_doubleArrow(_extension, _ext));
        
      }
    };
    Each.<FilesFileExtensionSpecExamples>forEach(this.examples, _function);
  }
}
