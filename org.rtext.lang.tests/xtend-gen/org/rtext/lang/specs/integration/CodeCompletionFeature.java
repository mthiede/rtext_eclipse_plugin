package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.CodeCompletionFeatureCodeCompletionForNestedElements;
import org.rtext.lang.specs.integration.CodeCompletionFeatureProposalSignalsBackendCurrentlyLoading;
import org.rtext.lang.specs.integration.CodeCompletionFeatureProposalSignalsBackendFailure;
import org.rtext.lang.specs.integration.CodeCompletionFeatureProposalSignalsBackendNotYetLoaded;
import org.rtext.lang.specs.integration.CodeCompletionFeatureSucessfullyUsingCodeCompletion;

@Contains({ CodeCompletionFeatureSucessfullyUsingCodeCompletion.class, CodeCompletionFeatureCodeCompletionForNestedElements.class, CodeCompletionFeatureProposalSignalsBackendCurrentlyLoading.class, CodeCompletionFeatureProposalSignalsBackendNotYetLoaded.class, CodeCompletionFeatureProposalSignalsBackendFailure.class })
@Named("Code completion")
@SuppressWarnings("all")
@RunWith(FeatureRunner.class)
public class CodeCompletionFeature {
}
