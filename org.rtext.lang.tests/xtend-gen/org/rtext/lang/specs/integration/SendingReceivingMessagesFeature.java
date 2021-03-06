package org.rtext.lang.specs.integration;

import org.jnario.runner.Contains;
import org.jnario.runner.FeatureRunner;
import org.jnario.runner.Named;
import org.junit.runner.RunWith;
import org.rtext.lang.specs.integration.SendingReceivingMessagesFeatureAnsynchronousCommunication;
import org.rtext.lang.specs.integration.SendingReceivingMessagesFeatureSynchronousCommunication;

@Contains({ SendingReceivingMessagesFeatureSynchronousCommunication.class, SendingReceivingMessagesFeatureAnsynchronousCommunication.class })
@Named("Sending & Receiving Messages")
@RunWith(FeatureRunner.class)
@SuppressWarnings("all")
public class SendingReceivingMessagesFeature {
}
