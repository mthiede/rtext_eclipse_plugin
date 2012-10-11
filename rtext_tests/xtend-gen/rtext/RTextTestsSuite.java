package rtext;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import rtext.RTextModelParserSpec;
import rtext.SyntaxScannerSpec;

@RunWith(ExampleGroupRunner.class)
@Named("RText Tests")
@Contains({ RTextModelParserSpec.class, SyntaxScannerSpec.class })
@SuppressWarnings("all")
public class RTextTestsSuite {
}
