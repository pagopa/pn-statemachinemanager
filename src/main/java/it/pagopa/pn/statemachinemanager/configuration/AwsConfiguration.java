package it.pagopa.pn.statemachinemanager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClientBuilder;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;


import java.net.URI;

import static it.pagopa.pn.statemachinemanager.constants.Constants.DEFAULT_CREDENTIALS_PROVIDER;

@Configuration
public class AwsConfiguration {

//  Set in LocalStackTestConfig

    @Value("${test.aws.region-code:#{null}}")
    String regionCode;

    @Value("${test.aws.dynamodb.endpoint:#{null}}")
    String dynamoDbLocalStackEndpoint;

    @Value("${test.aws.cloudwatch.endpoint:#{null}}")
    String cloudwatchLocalStackEndpoint;



    private static final DefaultCredentialsProvider DEFAULT_CREDENTIALS_PROVIDER_V2 = DefaultCredentialsProvider.create();


    @Bean
    public DynamoDbClient dynamoDbClient() {
        var dynamoDbClientBuilder = DynamoDbClient.builder().credentialsProvider(DEFAULT_CREDENTIALS_PROVIDER);

        if (dynamoDbLocalStackEndpoint != null) {
            dynamoDbClientBuilder.endpointOverride(URI.create(dynamoDbLocalStackEndpoint));
        }
        if(regionCode != null) {
            dynamoDbClientBuilder.region(Region.of(regionCode));
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

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        CloudWatchAsyncClientBuilder cloudWatchAsyncClientBuilder = CloudWatchAsyncClient.builder()
                .credentialsProvider(DEFAULT_CREDENTIALS_PROVIDER_V2);


        if (cloudwatchLocalStackEndpoint != null) {
            cloudWatchAsyncClientBuilder.endpointOverride(URI.create(cloudwatchLocalStackEndpoint));
        }

        if(regionCode != null) {
            cloudWatchAsyncClientBuilder.region(Region.of(regionCode));
        }

        return cloudWatchAsyncClientBuilder.build();
    }
}
