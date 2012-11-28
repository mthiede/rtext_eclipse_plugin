package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

@Named("RText Tests")
@Contains({ ProgressSuite.class, RegressionSuite.class })
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
public class RTextTestsSuite {
}
