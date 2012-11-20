package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.CodeCompletionFeature;
import org.rtext.lang.specs.integration.CommunicationWithBackendSpec;
import org.rtext.lang.specs.integration.ProblemMarkersFeature;

@Named("Progress")
@Contains({ CodeCompletionFeature.class, CommunicationWithBackendSpec.class, ProblemMarkersFeature.class })
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
public class ProgressSuite {
}
