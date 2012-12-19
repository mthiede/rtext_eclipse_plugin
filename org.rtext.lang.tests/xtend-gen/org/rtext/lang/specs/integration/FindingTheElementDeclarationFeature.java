package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureDisconnectedBackend;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureHyperlinkHighlighting;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureOpenAnHyperlink;

@Contains({ FindingTheElementDeclarationFeatureOpenAnHyperlink.class, FindingTheElementDeclarationFeatureHyperlinkHighlighting.class, FindingTheElementDeclarationFeatureDisconnectedBackend.class })
@Named("Finding the element declaration")
@SuppressWarnings("all")
@RunWith(FeatureRunner.class)
public class FindingTheElementDeclarationFeature {
}
