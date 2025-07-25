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

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;


@TestConfiguration
@CustomLog
public class LocalStackTestConfig {
    static LocalStackContainer localStackContainer =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.0.4"))
                    .withServices(LocalStackContainer.Service.DYNAMODB, LocalStackContainer.Service.CLOUDWATCH)
                    .withClasspathResourceMapping("testcontainers/init.sh",
                            "/docker-entrypoint-initaws.d/make-storages.sh", BindMode.READ_ONLY)
                    .withClasspathResourceMapping("testcontainers/credentials",
                            "/root/.aws/credentials", BindMode.READ_ONLY)
                    .withNetworkAliases("localstack")
                    .withNetwork(Network.builder().build())
                    .waitingFor(Wait.forLogMessage(".*Initialization terminated.*", 1));

    static {
        localStackContainer.start();
        System.setProperty("aws.endpoint-url", localStackContainer.getEndpointOverride(DYNAMODB).toString());
        System.setProperty("test.aws.cloudwatch.endpoint", String.valueOf(localStackContainer.getEndpointOverride(CLOUDWATCH)));
        System.setProperty("test.aws.dynamodb.endpoint", String.valueOf(localStackContainer.getEndpointOverride(DYNAMODB)));
        try {
            System.setProperty("aws.sharedCredentialsFile", new ClassPathResource("testcontainers/credentials").getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
