package it.pagopa.pn.statemachinemanager.testutils.localstack;

import it.pagopa.pn.statemachinemanager.model.Transaction;
import it.pagopa.pn.statemachinemanager.testutils.exception.DynamoDbInitTableCreationException;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import javax.annotation.PostConstruct;
import java.util.Map;

import static java.util.Map.entry;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static software.amazon.awssdk.services.dynamodb.model.TableStatus.ACTIVE;

@TestConfiguration
@CustomLog
public class LocalStackTestConfig {

    static DockerImageName dockerImageName = DockerImageName.parse("localstack/localstack:1.0.4");
    static LocalStackContainer localStackContainer = new LocalStackContainer(dockerImageName).withServices(DYNAMODB);


    static {
        localStackContainer.start();

        System.setProperty("pn.sm.table.transaction", "Transaction");
        System.setProperty("test.aws.dynamodb.endpoint", String.valueOf(localStackContainer.getEndpointOverride(DYNAMODB)));
    }

    @PostConstruct
    public void initLocalStack() {
        initDynamo();
    }

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Autowired
    private DynamoDbWaiter dynamoDbWaiter;

    @Value("${pn.sm.table.transaction}")
    private String pnSmTableTransaction;

    private void initDynamo() {
        log.info("<-- START initLocalStack.initDynamo -->");

        var tableNameWithEntityClass = Map.ofEntries(entry(pnSmTableTransaction, Transaction.class));

        tableNameWithEntityClass.forEach((tableName, entityClass) -> {
            try {
                DescribeTableResponse describeTableResponse = dynamoDbClient.describeTable(builder -> builder.tableName(tableName));
                if (describeTableResponse.table().tableStatus() == ACTIVE) {
                    log.info("Table {} already created on local stack dynamo db", tableName);
                }
            } catch (ResourceNotFoundException resourceNotFoundException) {
                log.info("Table {} not found on first dynamo init. Proceed to create", tableName);
                createTable(tableName, entityClass);
            }
        });
    }

    private void createTable(final String tableName, final Class<?> entityClass) {
        DynamoDbTable<?> dynamoDbTable = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(entityClass));
        dynamoDbTable.createTable(builder -> builder.provisionedThroughput(b -> b.readCapacityUnits(5L).writeCapacityUnits(5L).build()));

        // La creazione delle tabelle su Dynamo è asincrona. Bisogna aspettare tramite il DynamoDbWaiter
        ResponseOrException<DescribeTableResponse> responseOrException =
                dynamoDbWaiter.waitUntilTableExists(builder -> builder.tableName(tableName).build()).matched();
        responseOrException.response().orElseThrow(() -> new DynamoDbInitTableCreationException(tableName));
    }
}
