package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.repositorymanager.constant.exception.StateManagerException;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Response;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Transaction;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StateMachineService {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${PnSmTableClientStates}")
    String pnSmmTableClientStates;


    public StateMachineService(DynamoDbEnhancedClient dynamoDbEnhancedClient) {

        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    public static final String SEPARATORE = "#";


    public Response queryTable(String processId, String currStatus, String clientId, String nextStatus) {
        String anyStatus = "_any_";

        this.validaRequest(processId, currStatus, nextStatus);
        Response resp = new Response();
        Transaction processClientId = new Transaction();
        if (!clientId.isEmpty()) {
            processClientId.setProcessClientId(processId + SEPARATORE + clientId);
        } else {
            processClientId.setProcessClientId(processId);
        }

        try {
            boolean notFound = true;
            DynamoDbTable<Transaction> transactionTable = dynamoDbEnhancedClient.table(pnSmmTableClientStates, TableSchema.fromBean(Transaction.class));

            Key fromKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(currStatus).build();
            Key anyKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(anyStatus).build();

            QueryConditional queryConditional = QueryConditional.keyEqualTo(fromKey);
            //QueryConditional queryConditional = QueryConditional. keyEqualTo(fromKey). sortBetween(fromKey, toKey);
            // Get items in the table and write out the ID value.
            Iterator<Transaction> results = transactionTable.query(queryConditional).items().iterator();

            if (!results.hasNext()) {

                queryConditional = QueryConditional.keyEqualTo(anyKey);
                results = transactionTable.query(queryConditional).items().iterator();
            }


            List<Transaction> result = new ArrayList<>();

            while (results.hasNext()) {
                notFound = false;
                Transaction rec = results.next();
                if (rec.getTargetStatus().contains(nextStatus)) {
                    result.add(rec);
                }
                log.info("The process of the movie is " + rec.getProcessClientId());
                log.info("The reqeust status to validate  is " + nextStatus);
                log.info("The target status information  is " + rec.getTargetStatus());

            }

            if (notFound) {
                throw new StateManagerException.ErrorRequestValidateNotFoundCurrentStatus(currStatus);
            }
            resp.setAllowed(!result.isEmpty());

            return resp;

        } catch(DynamoDbException e){
            log.error(e.getMessage());
            System.exit(1);
            return resp;
        }
    }

    public ExternalStatusResponse getExternalStatus(String processId, String currStatus, String clientId){

        this.validateRequest(processId, currStatus);
        Transaction processClientId = new Transaction();
        ExternalStatusResponse resp = new ExternalStatusResponse();

        if (!clientId.isEmpty()) {
            processClientId.setProcessClientId(processId + SEPARATORE + clientId);
        } else {
            processClientId.setProcessClientId(processId);
        }

        try {

            DynamoDbTable<Transaction> transactionTable = dynamoDbEnhancedClient.table(pnSmmTableClientStates, TableSchema.fromBean(Transaction.class));

            Key key = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(currStatus).build();

            QueryConditional queryConditional = QueryConditional.keyEqualTo(key);

            Iterator<Transaction> results = transactionTable.query(queryConditional).items().iterator();

            if(results.hasNext()){
                Transaction element = results.next();
                resp.setExternalStatus(element.getExternalStatus());
                resp.setLogicStatus(element.getLogicStatus());

            } else {
                log.info("element not found ");
                throw new StateManagerException.ErrorRequestValidateNotFoundCurrentStatus(processClientId.getProcessClientId());
            }

            return resp;

        } catch(DynamoDbException e){
            log.error(e.getMessage());
            log.info("try catch error ");
            System.exit(1);
            return resp;
        }
    }

    private void validaRequest(String processId, String currStatus, String nextStatus){
        if (processId == null || processId.isEmpty() || processId.isBlank()) {
            log.info("Errore validazione dati proccessId : " + processId);
            throw new StateManagerException.ErrorRequestValidateProccesId(processId);
        } else if (currStatus == null || currStatus.isEmpty() || currStatus.isBlank()) {
            log.info("Errore validazione dati currStatus : " + currStatus);
            throw new StateManagerException.ErrorRequestValidateCurrentStatus(currStatus);

        } else if (nextStatus == null || nextStatus.isEmpty() || nextStatus.isBlank()) {
            log.info("Errore validazione dati nextStatus : " + nextStatus);
            throw new StateManagerException.ErrorRequestValidateNotFoundNextStatus(nextStatus);
        }
    }

    private void validateRequest(String processId, String currStatus){
        if (processId == null || processId.isEmpty() || processId.isBlank()) {
            log.info("Errore validazione dati proccessId : " + processId);
            throw new StateManagerException.ErrorRequestValidateProccesId(processId);
        } else if (currStatus == null || currStatus.isEmpty() || currStatus.isBlank()) {
            log.info("Errore validazione dati currStatus : " + currStatus);
            throw new StateManagerException.ErrorRequestValidateCurrentStatus(currStatus);
        }
    }



}
