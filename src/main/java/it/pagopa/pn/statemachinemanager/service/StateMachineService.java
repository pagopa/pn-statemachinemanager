package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.model.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Iterator;

@Service
@Slf4j
public class StateMachineService {

    private final DynamoDbTable<Transaction> transactionTable;

    public StateMachineService(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                               @Value("${pn.sm.table.transaction}") String pnSmTableTransaction) {
        this.transactionTable = dynamoDbEnhancedClient.table(pnSmTableTransaction, TableSchema.fromBean(Transaction.class));
    }

    private static final String SEPARATORE = "#";
    private static final String ANY_STATUS = "_any_";
    private static final String S_LOG_DEF = "Validate - processId = %s, clientId = %s, currStatus = %s, nextStatus = %s";

    public Response queryTable(String processId, String currStatus, String clientId, String nextStatus) throws StateMachineManagerException{


        checkNextStatus(nextStatus);

        Response resp = new Response();
        Transaction processClientId = new Transaction();

        try {
            boolean notFound = true;
            boolean boAllowed = false;

            int iCase = 0;

            Key oKey;
            QueryConditional queryConditional;
            Iterator<Transaction> results;

            String sLog;
            while (iCase < 4) {
                switch (iCase) {
                    case 0: { // processId + clientId + currStatus
                        if (clientId.isEmpty()) {
                            iCase = 2;
                            continue;
                        }
                        processClientId.setProcessClientId(processId + SEPARATORE + clientId);
                        oKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(currStatus).build();
                        sLog=String.format(S_LOG_DEF, processId, clientId, currStatus, nextStatus);
                        break;
                    }
                    case 1: { // processId + clientId + anyStatus

                        processClientId.setProcessClientId(processId + SEPARATORE + clientId);
                        oKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(ANY_STATUS).build();
                        sLog=String.format(S_LOG_DEF, processId, clientId, ANY_STATUS, nextStatus);
                        break;
                    }
                    case 2: { // processId + currStatus
                        processClientId.setProcessClientId(processId);
                        oKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(currStatus).build();
                        sLog=String.format(S_LOG_DEF, processId, "", currStatus, nextStatus);
                        break;
                    }
                    case 3: { // processId + anyStatus
                        processClientId.setProcessClientId(processId);
                        oKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(ANY_STATUS).build();
                        sLog=String.format(S_LOG_DEF, processId, "", ANY_STATUS, nextStatus);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unexpected value: " + iCase);
                }
                queryConditional = QueryConditional.keyEqualTo(oKey);
                results = transactionTable.query(queryConditional).items().iterator();

                if (results.hasNext()) {
                    notFound = false;
                    Transaction rec = results.next();
                    if (rec.getTargetStatus().contains(nextStatus)) {
                        log.debug("Valid transition: " + sLog);
                        boAllowed = true;
                        break;
                    } else if (rec.getTargetStatus().contains(ANY_STATUS)) {
                        log.debug("to any transition: " + sLog);
                        boAllowed = true;
                        break;
                    }
                    log.debug("Invalid transition: " + sLog);
                } else {
                    log.debug("Item not found: " + sLog);
                }
                iCase++;
            }

            if (notFound) {
                throw new StateMachineManagerException.ErrorRequestValidateNotFoundCurrentStatus(currStatus);
            }
            resp.setAllowed(boAllowed);

            return resp;

        } catch (DynamoDbException e) {
            log.error("DynamoDbException in queryTable method {}", e.getMessage());
            System.exit(1);
            return resp;
        }
    }


    private void checkNextStatus(String nextStatus) throws StateMachineManagerException {
        final String NEXT_STATUS = "nextStatus";
        log.info("Checking {}", NEXT_STATUS);
        if (nextStatus == null || nextStatus.isEmpty() || nextStatus.isBlank()) {
            log.warn("{} failed error = ErrorRequestValidateNotFoundNextStatus, {} ", NEXT_STATUS, nextStatus);
            throw new StateMachineManagerException.ErrorRequestValidateNotFoundNextStatus(nextStatus);
        }
        log.info("Checking {} passed", NEXT_STATUS);
    }
    public ExternalStatusResponse getExternalStatus(String processId, String currStatus, String clientId) {

        Transaction processClientId = new Transaction();
        ExternalStatusResponse resp = new ExternalStatusResponse();

        try {
            boolean notFound = true;
            int iCase = 0;

            Key oKey;
            QueryConditional queryConditional;
            Iterator<Transaction> results;

            String sLog;
            while (iCase < 2) {
                switch (iCase) {
                    case 0: { // processId + clientId + currStatus
                        if (clientId.isEmpty()) {
                            iCase = 1;
                            continue;
                        }
                        processClientId.setProcessClientId(processId + SEPARATORE + clientId);
                        oKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(currStatus).build();
                        sLog = "Decode - ProcessId=\"" + processId + "\" ClientId=\"" + clientId + "\" currStatus=\"" + currStatus + "\"";
                        break;
                    }
                    case 1: { // processId + clientId + anyStatus
                        processClientId.setProcessClientId(processId);
                        oKey = Key.builder().partitionValue(processClientId.getProcessClientId()).sortValue(currStatus).build();
                        sLog = "Decode - ProcessId=\"" + processId + "\" currStatus=\"" + currStatus + "\"";
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unexpected value: " + iCase);
                }
                queryConditional = QueryConditional.keyEqualTo(oKey);
                results = transactionTable.query(queryConditional).items().iterator();

                if (results.hasNext()) {
                    notFound = false;
                    Transaction element = results.next();
                    resp.setExternalStatus(element.getExternalStatus());
                    resp.setLogicStatus(element.getLogicStatus());
                    log.debug("Item found: " + sLog);
                } else {
                    log.debug("Item not found: " + sLog);
                }
                iCase++;
            }

            if (notFound) {
                throw new StateMachineManagerException.ErrorRequestValidateNotFoundCurrentStatus(processClientId.getProcessClientId());
            }

            return resp;

        } catch (DynamoDbException e) {
            log.error("DynamoDbException in getExternalStatus method {}", e.getMessage());
            System.exit(1);
            return resp;
        }
    }
}
