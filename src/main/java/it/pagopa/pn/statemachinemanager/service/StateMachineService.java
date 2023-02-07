package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.repositorymanager.constant.exception.StateManagerException;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Response;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Transaction;
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

    @Value("${PnSmTableClientStates}")
    String pnSmmTableClientStates;


    public StateMachineService(DynamoDbEnhancedClient dynamoDbEnhancedClient) {

        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.pnSmmTableClientStates = pnSmmTableClientStates;
    }
    public static final String SEPARATORE = "#";


    public Response queryTable(String processId, String currStatus ,String clientId,String nextStatus)  {
        Response resp = new Response();
        Transaction processClientId =  new Transaction();
        if (!clientId.isEmpty() && clientId != null){
            processClientId.setProcessClientId(processId + SEPARATORE +clientId);
        }else {
            processClientId.setProcessClientId(processId);
        }

        try {

            DynamoDbTable<Transaction> transactionTable = dynamoDbEnhancedClient.table(pnSmmTableClientStates, TableSchema.fromBean(Transaction.class));
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder()
                            .partitionValue(processClientId.getProcessClientId()).sortValue(currStatus)
                            .build());

            // Get items in the table and write out the ID value.
            Iterator<Transaction> results = transactionTable.query(queryConditional).items().iterator();

                if (!results.hasNext()) {

                    processClientId.setProcessClientId(processId);
                    queryConditional = QueryConditional
                            .keyEqualTo(Key.builder()
                                    .partitionValue(processClientId.getProcessClientId()).sortValue(currStatus)
                                    .build());
                    results = transactionTable.query(queryConditional).items().iterator();
                }


            List<Transaction> result = new ArrayList<>();

            while (results.hasNext()) {
                Transaction rec = results.next();
                result.add(rec);
                System.out.println("The process of the movie is "+rec.getProcessClientId());
                System.out.println("The reqeust status to validate  is "+nextStatus);
                System.out.println("The target status information  is "+rec.getTargetStatus());

            }

            if(!result.isEmpty() ){
                resp.setAllowed(true);
            }else {
                throw new StateManagerException.ErrorRequestValidate( clientId);
            }

            return resp;

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return resp;
        }
    }

}
