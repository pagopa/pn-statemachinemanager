package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.model.Transaction;
import it.pagopa.pn.statemachinemanager.testutils.annotation.SpringBootTestWebEnv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTestWebEnv
@AutoConfigureWebTestClient
class StateMachineServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Value("${pn.sm.table.transaction}")
    private String pnSmTableTransaction;

    @BeforeEach
    void setUp() {
        try {
            var transactionDynamoDbTable = enhancedClient.table(pnSmTableTransaction, TableSchema.fromBean(Transaction.class));

            // Populate the Table.
            List<String> list = new ArrayList<>();
            list.add("VALIDATE");
            var transaction = new Transaction();

            transaction.setProcessClientId("INVIO_PEC#C050");
            transaction.setCurrStatus("BOOKED");
            transaction.setTargetStatus(list);

            // Put the customer data into an Amazon DynamoDB table.
            transactionDynamoDbTable.putItem(transaction);

            list = new ArrayList<>();
            list.add("_any_");
            transaction.setProcessClientId("INVIO_PEC#C050");
            transaction.setCurrStatus("SENT");
            transaction.setTargetStatus(list);

            // Put the customer data into an Amazon DynamoDB table.
            transactionDynamoDbTable.putItem(transaction);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Customer data added to the table with id id101");
    }

    @Test
    void getStatusTest() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        var nextStatus = "VALIDATE";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                          "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(Response.class);
    }

    @Test
    void getStatusToAnyOk() {
        var process = "INVIO_PEC";
        var currStato = "SENT";
        var clientId = "C050";
        var nextStatus = "fake";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                          "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.allowed")
                     .isEqualTo("true");
    }

    @Test
    void getStatusToAnyKO() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        var nextStatus = "fake";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                          "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.allowed")
                     .isEqualTo("false");
    }

    @Test
    void getStatusTestKOClientId() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C05";
        var nextStatus = "COMPOSED";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                          "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(StateMachineManagerException.ErrorRequestValidateNotFoundClientId.class);
    }


    @Test
    void getStatusTestKOCurrenStatus() {
        var process = "PEC";
        var clientId = "C05";
        var nextStatus = "COMPOSED";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/?clientId=" + clientId + "&nextStatus=" +
                          nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(StateMachineManagerException.ErrorRequestValidateNotFoundCurrentStatus.class);
    }

    @Test
    @Order(5)
    void getStatusTestKONextStatus() {
        var process = "PEC";
        var currStato = "BOOKED";
        var clientId = "C05";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                          "&nextStatus=" + null)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound()
                     .expectBody(StateMachineManagerException.ErrorRequestValidateNotFoundNextStatus.class);
    }
}
