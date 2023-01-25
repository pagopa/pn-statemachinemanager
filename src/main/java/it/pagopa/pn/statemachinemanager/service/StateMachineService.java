package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StateMachineService {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${PnSmmTableClientStates}")
    String pnSmmTableClientStates;


    public StateMachineService(DynamoDbEnhancedClient dynamoDbEnhancedClient) {

        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.pnSmmTableClientStates = pnSmmTableClientStates;
    }
    public static final String SEPARATORE = "#";


    public Response queryTable(String partition_id, String sort_id ,String clientId,String nextStatus) {
        Response resp = new Response();
        Transaction processClientId =  new Transaction();
        processClientId.setProcessClientId(partition_id + SEPARATORE +clientId);
        try {

            DynamoDbTable<Transaction> transactionTable = dynamoDbEnhancedClient.table(pnSmmTableClientStates, TableSchema.fromBean(Transaction.class));
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder()
                            .partitionValue(processClientId.getProcessClientId()).sortValue(sort_id)
                            .build());

            // Get items in the table and write out the ID value.
            Iterator<Transaction> results = transactionTable.query(queryConditional).items().iterator();


            List<Transaction> result = new ArrayList<>();
            while (results.hasNext()) {
                Transaction rec = results.next();
                result.add(rec);
                System.out.println("The process of the movie is "+rec.getProcessClientId());
                System.out.println("The reqeust status to validate  is "+nextStatus);
                System.out.println("The target status information  is "+rec.getTargetStatus());

            }

            if(result.get(0).getTargetStatus().contains(nextStatus)){
                resp.setAllowed(true);
//                result.get(0).setAllowed(resp.isAllowed());
            }

            return resp;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return resp;
        }
    }

}
