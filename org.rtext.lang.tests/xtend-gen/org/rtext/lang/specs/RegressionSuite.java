package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.unit.RTextModelParserSpec;
import org.rtext.lang.specs.unit.SyntaxScannerSpec;

@RunWith(ExampleGroupRunner.class)
@Named("Regression")
@Contains({ RTextModelParserSpec.class, SyntaxScannerSpec.class })
@SuppressWarnings("all")
public class RegressionSuite {
}
