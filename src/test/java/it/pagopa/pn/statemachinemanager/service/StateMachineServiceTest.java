package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.configuration.PnStateMachineManagerConfig;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class StateMachineServiceTest {

    @Test
    void transactionTableNameIsTakenFromConfiguration() {
        DynamoDbEnhancedClient dynamoDbEnhancedClient = mock(DynamoDbEnhancedClient.class);

        PnStateMachineManagerConfig config = new PnStateMachineManagerConfig();
        PnStateMachineManagerConfig.Table table = new PnStateMachineManagerConfig.Table();
        table.setTransaction("pn-SmStates-test");
        config.setTable(table);

        new StateMachineService(dynamoDbEnhancedClient, config);

        verify(dynamoDbEnhancedClient).table(eq("pn-SmStates-test"), any(TableSchema.class));
    }
}