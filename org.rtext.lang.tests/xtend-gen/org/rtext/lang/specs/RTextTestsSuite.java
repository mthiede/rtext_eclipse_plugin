package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.ProgressSuite;
import org.rtext.lang.specs.RegressionSuite;

@RunWith(ExampleGroupRunner.class)
@Named("RText Tests")
@Contains({ ProgressSuite.class, RegressionSuite.class })
@SuppressWarnings("all")
public class RTextTestsSuite {
}
