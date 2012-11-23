package org.rtext.lang.specs;

import org.jnario.runner.Contains;
import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.CodeCompletionFeature;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeature;
import org.rtext.lang.specs.integration.ProblemMarkersFeature;
import org.rtext.lang.specs.integration.SendingReceivingMessagesFeature;

@Named("Progress")
@Contains({ CodeCompletionFeature.class, FindingTheElementDeclarationFeature.class, ProblemMarkersFeature.class, SendingReceivingMessagesFeature.class })
@SuppressWarnings("all")
@RunWith(ExampleGroupRunner.class)
public class ProgressSuite {
}
