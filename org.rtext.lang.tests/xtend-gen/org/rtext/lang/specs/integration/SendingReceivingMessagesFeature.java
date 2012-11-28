package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;

@RunWith(FeatureRunner.class)
@Contains({ SendingReceivingMessagesFeatureSynchronousCommunication.class, SendingReceivingMessagesFeatureAnsynchronousCommunication.class })
@Named("Sending & Receiving Messages")
@SuppressWarnings("all")
public class SendingReceivingMessagesFeature {
}
