package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Contains({ FindingTheElementDeclarationFeatureOpenAnHyperlink.class, FindingTheElementDeclarationFeatureHyperlinkHighlighting.class, FindingTheElementDeclarationFeatureDisconnectedBackend.class })
@Named("Finding the element declaration")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeature {
}
