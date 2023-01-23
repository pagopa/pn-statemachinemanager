package it.pagopa.pn.statemachinemanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

import java.net.URI;

@Configuration
public class AwsConfiguration {

    /**
     * Set in SQSLocalStackTestConfig
     */
    @Value("${aws.sqs.test.endpoint:#{null}}")
    String sqsLocalStackEndpoint;

    /**
     * Set in DynamoDbLocalStackTestConfig
     */
    @Value("${aws.dynamodb.test.endpoint:#{null}}")
    String dynamoDbLocalStackEndpoint;

    @Bean
    public SqsClient getSqsClient() {
        SqsClientBuilder sqsClientBuilder = SqsClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create());

        if (sqsLocalStackEndpoint != null) {
            sqsClientBuilder.endpointOverride(URI.create(sqsLocalStackEndpoint));
        }

        return sqsClientBuilder.build();
    }

    @Bean
    public DynamoDbClient getDynamoDbClient() {
        DynamoDbClientBuilder dynamoDbClientBuilder = DynamoDbClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create());

        if (dynamoDbLocalStackEndpoint != null) {
            dynamoDbClientBuilder.endpointOverride(URI.create(dynamoDbLocalStackEndpoint));
        }

        return dynamoDbClientBuilder.build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    public DynamoDbWaiter getDynamoDbWaiter(DynamoDbClient dynamoDbClient) {
        return DynamoDbWaiter.builder().client(dynamoDbClient).build();
    }
}
