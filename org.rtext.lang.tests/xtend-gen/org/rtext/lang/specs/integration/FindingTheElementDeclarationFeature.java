package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureFindDeclarationWithinSameFile;
import org.rtext.lang.specs.integration.FindingTheElementDeclarationFeatureHyperlinkHighlighting;

@RunWith(FeatureRunner.class)
@Contains({ FindingTheElementDeclarationFeatureFindDeclarationWithinSameFile.class, FindingTheElementDeclarationFeatureHyperlinkHighlighting.class })
@Named("Finding the element declaration")
@SuppressWarnings("all")
public class FindingTheElementDeclarationFeature {
}
