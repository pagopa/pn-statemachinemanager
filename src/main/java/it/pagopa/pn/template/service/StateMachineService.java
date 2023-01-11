package it.pagopa.pn.template.service;

import it.pagopa.pn.template.factory.DependencyFactory;
import it.pagopa.pn.template.model.Response;
import it.pagopa.pn.template.model.Transaction;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StateMachineService {

    public StateMachineService() {

    }


    public Response queryTable(String partition_id, String sort_id ,String nextStatus) {
        Response resp = new Response();
        try {
            DynamoDbEnhancedClient enhancedClient = DependencyFactory.dynamoDbEnhancedClient();

            DynamoDbTable<Transaction> transactionTable = enhancedClient.table("Transaction", TableSchema.fromBean(Transaction.class));
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder()
                            .partitionValue(partition_id).sortValue(sort_id)
                            .build());

            // Get items in the table and write out the ID value.
            Iterator<Transaction> results = transactionTable.query(queryConditional).items().iterator();


            List<Transaction> result = new ArrayList<>();
            while (results.hasNext()) {
                Transaction rec = results.next();
                result.add(rec);
                System.out.println("The process of the movie is "+rec.getProcessClientId());
                System.out.println("The target status information  is "+rec.getTargetStatus());
            }

            if(result.get(0).getTargetStatus().contains(nextStatus)){
                resp.setAllowed(true);
                result.get(0).setAllowed(resp.isAllowed());
            }

            return resp;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            resp.setAllowed(false);
            return null;
        }
    }

}
