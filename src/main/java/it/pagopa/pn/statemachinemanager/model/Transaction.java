package it.pagopa.pn.statemachinemanager.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@DynamoDbBean
public class Transaction {

    @Getter(onMethod = @__({@DynamoDbPartitionKey}))
    String processClientId;

    @Getter(onMethod = @__({@DynamoDbSortKey}))
    String currStatus;

    List<String> targetStatus;
    String externalStatus;
    String logicStatus;
}
