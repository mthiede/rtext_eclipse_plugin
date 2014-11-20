/**
 * Copyright (c) 2012 E.S.R. Labs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 */
package org.rtext.lang.specs.unit.parser;

import java.util.List;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.jnario.lib.Assert;
import org.jnario.lib.Should;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtext.lang.editor.IColorConstants;
import org.rtext.lang.specs.unit.parser.SyntaxScannerSpec;

@Named("incremental parsing")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class SyntaxScannerIncrementalParsingSpec extends SyntaxScannerSpec {
  @Test
  @Named("after comma")
  @Order(1)
  public void _afterComma() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name,");
    _builder.newLine();
    _builder.append("|label: \"a string\"");
    _builder.newLine();
    List<RGB> _incrementalScan = this.incrementalScan(_builder);
    RGB _get = _incrementalScan.get(0);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t\t\tType name,\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\'.incrementalScan.get(0) => LABEL but"
     + "\n     \'\'\'\n\t\t\t\tType name,\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\'.incrementalScan.get(0) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\t\t\tType name,\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\'.incrementalScan is " + new org.hamcrest.StringDescription().appendValue(_incrementalScan).toString()
     + "\n     \'\'\'\n\t\t\t\tType name,\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     LABEL is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.LABEL).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.LABEL));
    
  }
  
  @Test
  @Named("after comma with empty line")
  @Order(2)
  public void _afterCommaWithEmptyLine() throws Exception {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Type name,");
    _builder.newLine();
    _builder.newLine();
    _builder.append("|label: \"a string\"");
    _builder.newLine();
    List<RGB> _incrementalScan = this.incrementalScan(_builder);
    RGB _get = _incrementalScan.get(0);
    Assert.assertTrue("\nExpected \'\'\'\n\t\t\t\tType name,\n\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\'.incrementalScan.get(0) => LABEL but"
     + "\n     \'\'\'\n\t\t\t\tType name,\n\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\'.incrementalScan.get(0) is " + new org.hamcrest.StringDescription().appendValue(_get).toString()
     + "\n     \'\'\'\n\t\t\t\tType name,\n\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\'.incrementalScan is " + new org.hamcrest.StringDescription().appendValue(_incrementalScan).toString()
     + "\n     \'\'\'\n\t\t\t\tType name,\n\n\t\t\t\t|label: \"a string\"\n\t\t\t\'\'\' is " + new org.hamcrest.StringDescription().appendValue(_builder).toString()
     + "\n     LABEL is " + new org.hamcrest.StringDescription().appendValue(IColorConstants.LABEL).toString() + "\n", Should.<RGB>operator_doubleArrow(_get, IColorConstants.LABEL));
    
  }
}
