package it.pagopa.pn.statemachinemanager.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PnStateMachineManagerConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ConfigurationPropertiesAutoConfiguration.class))
            .withUserConfiguration(PnStateMachineManagerConfig.class);

    @Test
    void tableTransactionIsBoundFromDomainProperty() {
        contextRunner
                .withPropertyValues("pn.sm.table.transaction=pn-SmStates-test")
                .run(context -> {
                    assertThat(context).hasSingleBean(PnStateMachineManagerConfig.class);
                    PnStateMachineManagerConfig config = context.getBean(PnStateMachineManagerConfig.class);
                    assertThat(config.getTable())
                            .as("pn.sm.table.transaction deve essere legata alla classe di configurazione")
                            .isNotNull();
                    assertThat(config.getTable().getTransaction()).isEqualTo("pn-SmStates-test");
                });
    }
}
