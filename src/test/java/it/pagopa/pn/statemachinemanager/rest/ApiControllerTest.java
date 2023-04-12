package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.model.Transaction;
import it.pagopa.pn.statemachinemanager.testutils.annotation.SpringBootTestWebEnv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTestWebEnv
@AutoConfigureWebTestClient
class ApiControllerTest {

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
            transaction.setExternalStatus("testExStatus");
            transaction.setLogicStatus("testLogic");

            // Put the customer data into an Amazon DynamoDB table.
            transactionDynamoDbTable.putItem(transaction);

            transaction.setProcessClientId("INVIO_PEC");
            transaction.setCurrStatus("SENT");
            transaction.setTargetStatus(list);
            transaction.setExternalStatus("testExtSent");
            transaction.setLogicStatus("testLogicSent");

            // Put the customer data into an Amazon DynamoDB table.
            transactionDynamoDbTable.putItem(transaction);

            List<String> list2 = new ArrayList<>();
            list2.add("INTERNAL_ERROR");

            transaction = new Transaction();
            transaction.setProcessClientId("INVIO_PEC#C050");
            transaction.setCurrStatus("_any_");
            transaction.setTargetStatus(list2);

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
    void getStatusTestKO() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        var nextStatus = "COMPOSED";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                      "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();
    }

    @Test
    void getStatusTestKOClientId() {
        var process = "INVIO_PEC";
        var currStato = "BOOKD";
        var clientId = "null";
        var nextStatus = "COMPOSED";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                      "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void getStatusTestKOProcessId() {
        var currStato = "BOOKED";
        var clientId = "C05";
        var nextStatus = "COMPOSED";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + null + "/" + currStato + "?clientId=" + clientId +
                      "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void getStatusTestKOCurrenStatus() {
        var process = "PEC";
        //String currStato = null;
        var clientId = "C05";
        var nextStatus = "COMPOSED";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/?clientId=" + clientId + "&nextStatus=" +
                      nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void getStatusTestKONextStatus() {
        var process = "PEC";
        var currStato = "BOOKE";
        var clientId = "C05";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                      "&nextStatus=" + null)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void getStatusTestANYCurrStatus() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        var nextStatus = "INTERNAL_ERROR";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/validate/" + process + "/" + currStato + "?clientId=" + clientId +
                      "&nextStatus=" + nextStatus)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();
    }

    @Test
    void getExternalStatusTestOk() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/decodeLogical/" + process + "/" + currStato + "?clientId=" + clientId)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();
    }

    @Test
    void getExternalStatusTestOkWOClient() {
        var process = "INVIO_PEC";
        var currStato = "SENT";
        var clientId = "";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/decodeLogical/" + process + "/" + currStato + "?clientId=" + clientId)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();
    }

    @Test
    void getExternalStatusTestKO() {
        var process = "INVIO_PEC_KO";
        var currStato = "BOOKED";
        var clientId = "C050";
        webTestClient.get()
                     .uri("http://localhost:8080/statemachinemanager/decodeLogical/" + process + "/" + currStato + "?clientId=" + clientId)
                     .accept(APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }
}

