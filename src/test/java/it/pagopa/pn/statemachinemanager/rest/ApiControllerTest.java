package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.model.Transaction;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import it.pagopa.pn.statemachinemanager.testutils.annotation.SpringBootTestWebEnv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTestWebEnv
@AutoConfigureWebTestClient
class ApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    StateMachineService service = mock(StateMachineService.class);


    @Value("${pn.sm.table.transaction}")
    private String pnSmTableTransaction;

    private final String Uri = "/statemachinemanager/validate/{process}/{currStato}";
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

            List<String> list3 = new ArrayList<>();
            list3.add("VALIDATE");
            list3.add("_end_");

            transaction = new Transaction();
            transaction.setProcessClientId("INVIO_PEC#C051");
            transaction.setCurrStatus("_any_");
            transaction.setTargetStatus(list3);

            transactionDynamoDbTable.putItem(transaction);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Customer data added to the table with id id101");
    }

    private WebTestClient.ResponseSpec webClientTestCall(String process, String currStato, String clientId, String nextStatus) {
        return webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(Uri)
                        .queryParam("clientId", clientId)
                        .queryParam("nextStatus", nextStatus)
                        .build(process, currStato))
                .accept(APPLICATION_JSON)
                .exchange();
    }


    @Test
    void getStatusTestAllowed() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        var nextStatus = "VALIDATE";
        webClientTestCall(process, currStato, clientId, nextStatus)
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .jsonPath("$.allowed")
                     .isEqualTo("true");
    }

    @Test
    void getEndStatusTestNotAllowed() {
        var process = "INVIO_PEC";
        var currStato = "_any_";
        var clientId = "C051";
        var nextStatus = "VALIDATE";
        webClientTestCall(process, currStato, clientId, nextStatus)
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.allowed")
                .isEqualTo("false");
    }

    @Test
    void getStatusTestNotAllowed() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var clientId = "C050";
        var nextStatus = "SENT";
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
    void getStatusTestKOProcessId() {
        var currStato = "BOOKED";
        var clientId = "C05";
        var nextStatus = "COMPOSED";
        webClientTestCall(null, currStato, clientId, nextStatus)
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void getStatusTestKOCurrentStatus() {
        var process = "PEC";
        var clientId = "C05";
        var nextStatus = "COMPOSED";
        webClientTestCall(process, null, clientId, nextStatus)
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void getStatusTestKOClientId() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var nextStatus = "COMPOSED";
        webClientTestCall(process, currStato, null, nextStatus)
                .expectStatus()
                .isBadRequest();
    }
    @Test
    void getStatusTestKOClientIdEmpty() {
        var process = "INVIO_PEC";
        var currStato = "BOOKED";
        var nextStatus = "COMPOSED";
        webClientTestCall(process, currStato, "", nextStatus)
                .expectStatus()
                .isNotFound();
    }
    @Test
    void getStatusTestKONextStatus() {
        var process = "PEC";
        var currStato = "BOOKED";
        var clientId = "C05";
        webClientTestCall(process, currStato, clientId, null)
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void getStatusTestKONextStatusBlank() {
        var process = "PEC";
        var currStato = "BOOKED";
        var clientId = "C05";
        webClientTestCall(process, currStato, clientId, "")
                .expectStatus()
                .isNotFound();
    }

    @ParameterizedTest
    @CsvSource({"INVIO_PEC, BOOKED, C050, INTERNAL_ERROR", "INVIO_PEC, SENT, \"\", INTERNAL_ERROR", "INVIO_PEC, SENT, C050, COMPOSED"})
    void getStatusTestANYEmptyClientId(String process, String currStato, String clientId, String nextStatus) {
        webClientTestCall(process, currStato, clientId, nextStatus)
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

