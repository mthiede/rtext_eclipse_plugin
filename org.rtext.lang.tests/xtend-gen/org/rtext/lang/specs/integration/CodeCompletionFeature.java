package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Contains({ CodeCompletionFeatureSucessfullyUsingCodeCompletion.class, CodeCompletionFeatureCodeCompletionForNestedElements.class, CodeCompletionFeatureProposalSignalsBackendFailure.class })
@Named("Code completion")
@SuppressWarnings("all")
public class CodeCompletionFeature {
}
