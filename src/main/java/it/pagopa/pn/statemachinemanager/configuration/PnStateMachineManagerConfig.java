package it.pagopa.pn.statemachinemanager.configuration;

import it.pagopa.pn.commons.conf.SharedAutoConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.CustomLog;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConfigurationProperties(prefix = "pn.sm")
@Data
@CustomLog
@Import({SharedAutoConfiguration.class})
public class PnStateMachineManagerConfig {

    private Table table;

    @Data
    public static class Table {
        private String transaction;
    }

    @PostConstruct
    public void init() {
        log.info("Configs: {}", this);
    }
}
