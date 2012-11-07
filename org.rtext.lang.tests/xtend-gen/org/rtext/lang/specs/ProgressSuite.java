package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.CommunicationWithBackendSpec;

@RunWith(ExampleGroupRunner.class)
@Named("Progress")
@Contains(CommunicationWithBackendSpec.class)
@SuppressWarnings("all")
public class ProgressSuite {
}
