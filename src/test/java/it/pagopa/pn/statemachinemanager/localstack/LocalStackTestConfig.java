package it.pagopa.pn.statemachinemanager.localstack;

import it.pagopa.pn.statemachinemanager.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import javax.annotation.PostConstruct;

import java.io.IOException;

import static it.pagopa.pn.statemachinemanager.repositorymanager.constant.DynamoTableNameConstant.TRANSACTION_TABLE_NAME;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;

@TestConfiguration
public class LocalStackTestConfig {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DynamoDbWaiter dynamoDbWaiter;

    static DockerImageName dockerImageName = DockerImageName.parse("localstack/localstack:1.0.4");
    static LocalStackContainer localStackContainer = new LocalStackContainer(dockerImageName).withServices(DYNAMODB)
            .withClasspathResourceMapping("testcontainers/innit.sh" , "/docker-entrypoint-initaws" + ".d/make-storages.sh", BindMode.READ_ONLY);

    static {
        localStackContainer.start();

        System.setProperty("AWS_REGIONCODE", localStackContainer.getRegion());
        System.setProperty("PnSmmTableClientStates", "Transaction");
        System.setProperty("test.aws.dynamodb.endpoint", String.valueOf(localStackContainer.getEndpointOverride(DYNAMODB)));
          }

    @PostConstruct
    public void createTable() {
        DynamoDbTable<Transaction> clientConfigurationTable = enhancedClient.table(TRANSACTION_TABLE_NAME,
                                                                                           TableSchema.fromBean(Transaction.class));

        clientConfigurationTable.createTable(builder -> builder.provisionedThroughput(b -> b.readCapacityUnits(5L)
                                                                                            .writeCapacityUnits(5L)
                                                                                            .build()));

        ResponseOrException<DescribeTableResponse> response = dynamoDbWaiter.waitUntilTableExists(builder -> builder.tableName(
                TRANSACTION_TABLE_NAME).build()).matched();
        DescribeTableResponse tableDescription = response.response()
                                                         .orElseThrow(() -> new RuntimeException("Customer table was not created."));
        // The actual error can be inspected in response.exception()
    }
}
