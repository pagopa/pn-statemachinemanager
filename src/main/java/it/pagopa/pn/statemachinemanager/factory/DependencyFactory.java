package it.pagopa.pn.statemachinemanager.factory;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DependencyFactory {

    private DependencyFactory() {}


    public static DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder().build();
    }

    public static DynamoDbEnhancedClient dynamoDbEnhancedClient(){
        DynamoDbClient ddb = DependencyFactory.dynamoDbClient();
        return DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();
    }

}
