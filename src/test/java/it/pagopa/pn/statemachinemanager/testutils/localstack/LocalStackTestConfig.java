package it.pagopa.pn.statemachinemanager.testutils.localstack;

import lombok.CustomLog;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static java.util.Map.entry;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.CLOUDWATCH;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;


@TestConfiguration
@CustomLog
public class LocalStackTestConfig {

    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.0.4"))
                    .withServices(LocalStackContainer.Service.DYNAMODB)
                    .withClasspathResourceMapping("testcontainers/init.sh",
                            "/docker-entrypoint-initaws.d/make-storages.sh", BindMode.READ_ONLY)
                    .withClasspathResourceMapping("testcontainers/credentials",
                            "/root/.aws/credentials", BindMode.READ_ONLY)
                    .withNetworkAliases("localstack")
                    .withNetwork(Network.builder().build())
                    .waitingFor(Wait.forLogMessage(".*Initialization terminated.*", 1));
    static DockerImageName dockerImageName = DockerImageName.parse("localstack/localstack:1.0.4");
    static LocalStackContainer localStackContainer = new LocalStackContainer(dockerImageName).withServices(DYNAMODB, CLOUDWATCH);


    static {
        localStack.start();
        System.setProperty("aws.endpoint-url", localStack.getEndpointOverride(DYNAMODB).toString());
        System.setProperty("pn.sm.table.transaction", "pn-SmStates");
        System.setProperty("test.aws.cloudwatch.endpoint", String.valueOf(localStackContainer.getEndpointOverride(CLOUDWATCH)));
        System.setProperty("test.aws.dynamodb.endpoint", String.valueOf(localStack.getEndpointOverride(DYNAMODB)));
        try {
            System.setProperty("aws.sharedCredentialsFile", new ClassPathResource("testcontainers/credentials").getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        localStackContainer.start();

        System.setProperty("pn.sm.table.transaction", "Transaction");
        System.setProperty("test.aws.cloudwatch.endpoint", String.valueOf(localStackContainer.getEndpointOverride(CLOUDWATCH)));
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

    }

}
