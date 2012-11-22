package org.rtext.lang.specs.unit;

import java.io.File;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.StringDescription;
import org.jnario.lib.ExampleTable;
import org.jnario.lib.ExampleTableIterators;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.unit.FilesFileExtensionSpecExamples;
import org.rtext.lang.specs.unit.FilesSpec;
import org.rtext.lang.util.Files;

@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
@Named("fileExtension")
public class FilesFileExtensionSpec extends FilesSpec {
  @Test
  @Named("^extension[null] throws IllegalArgumentException")
  @Order(1)
  public void _extensionNullThrowsIllegalArgumentException() throws Exception {
    try{
      Files.extension(null);
      Assert.fail("Expected " + IllegalArgumentException.class.getName() + " in \n     ^extension(null)\n with:"
       + "\n     ^extension(null) is " + new StringDescription().appendValue(Files.extension(null)).toString());
    }catch(IllegalArgumentException e){
    }
  }
  
  public ExampleTable<FilesFileExtensionSpecExamples> _initFilesFileExtensionSpecExamples() {
    return ExampleTable.create("examples", 
      java.util.Arrays.asList("filename", "ext"), 
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"\"", "\"\""), "", ""),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"file.txt\"", "\"*.txt\""), "file.txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"file.txt\"", "\"*.txt\""), "file.txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"test.file.txt\"", "\"*.txt\""), "test.file.txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"txt\"", "\"txt\""), "txt", "txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"/folder/file.txt\"", "\"*.txt\""), "/folder/file.txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\".txt\"", "\"*.txt\""), ".txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"/folder/.txt\"", "\"*.txt\""), "/folder/.txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"\\\\folder\\\\.txt\"", "\"*.txt\""), "\\folder\\.txt", "*.txt"),
      new FilesFileExtensionSpecExamples(  java.util.Arrays.asList("\"\\\\folder\\\\txt\"", "\"txt\""), "\\folder\\txt", "txt")
    );
  }
  
  protected ExampleTable<FilesFileExtensionSpecExamples> examples = _initFilesFileExtensionSpecExamples();
  
  @Test
  @Named("examples.forEach[new File[filename].^extension => ext]")
  @Order(2)
  public void _examplesForEachNewFileFilenameExtensionExt() throws Exception {
    final Procedure1<FilesFileExtensionSpecExamples> _function = new Procedure1<FilesFileExtensionSpecExamples>() {
        public void apply(final FilesFileExtensionSpecExamples it) {
          File _file = new File(it.filename);
          String _extension = Files.extension(_file);
          boolean _doubleArrow = Should.operator_doubleArrow(_extension, it.ext);
          Assert.assertTrue("\nExpected new File(filename).^extension => ext but"
           + "\n     new File(filename).^extension is " + new StringDescription().appendValue(_extension).toString()
           + "\n     new File(filename) is " + new StringDescription().appendValue(_file).toString()
           + "\n     filename is " + new StringDescription().appendValue(it.filename).toString()
           + "\n     ext is " + new StringDescription().appendValue(it.ext).toString() + "\n", _doubleArrow);
          
        }
      };
    ExampleTableIterators.<FilesFileExtensionSpecExamples>forEach(this.examples, _function);
  }
}
