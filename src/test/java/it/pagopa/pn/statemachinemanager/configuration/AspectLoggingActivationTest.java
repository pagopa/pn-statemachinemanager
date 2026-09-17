package it.pagopa.pn.statemachinemanager.configuration;

import it.pagopa.pn.commons.utils.ClientAspectLogging;
import it.pagopa.pn.commons.utils.ServerAspectLogging;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class AspectLoggingActivationTest {

    @Test
    void serverAspectLoggingActivationIsRegisteredAsBean() {
        new ApplicationContextRunner()
                .withUserConfiguration(ServerAspectLoggingActivation.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(ServerAspectLoggingActivation.class);
                    assertThat(context.getBean(ServerAspectLoggingActivation.class))
                            .isInstanceOf(ServerAspectLogging.class);
                });
    }

    @Test
    void clientAspectLoggingActivationIsRegisteredAsBean() {
        new ApplicationContextRunner()
                .withUserConfiguration(ClientAspectLoggingActivation.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(ClientAspectLoggingActivation.class);
                    assertThat(context.getBean(ClientAspectLoggingActivation.class))
                            .isInstanceOf(ClientAspectLogging.class);
                });
    }
}
