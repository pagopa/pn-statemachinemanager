package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.annotation.SpringBootTestWebEnv;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Response;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Transaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiControllerTest {



    @Autowired
    private WebTestClient webClient;


    @Autowired
    DynamoDbEnhancedClient enhancedClient;


    @BeforeEach
    void setUp (){
        try {
            DynamoDbTable<Transaction> custTable = enhancedClient.table("Transaction", TableSchema.fromBean(Transaction.class));


            // Populate the Table.
            List<String> list = new ArrayList<>();
            list.add("VALIDATE");
            Transaction transaction = new Transaction();

            transaction.setProcessClientId("INVIO_PEC#C050");
            transaction.setCurrStatus("BOOKED");
            transaction.setTargetStatus(list);

            // Put the customer data into an Amazon DynamoDB table.
            custTable.putItem(transaction);

            List<String> list2 = new ArrayList<>();
            list2.add("INTERNAL_ERROR");

            transaction  = new Transaction();
            transaction.setProcessClientId("INVIO_PEC#C050");
            transaction.setCurrStatus("_any_");
            transaction.setTargetStatus(list2);

            custTable.putItem(transaction);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Customer data added to the table with id id101");
    }

    @Test
    @Order(1)
    void getStatusTest() {
        String process = "INVIO_PEC";
        String currStato = "BOOKED";
        String clientId = "C050";
        String nextStatus = "VALIDATE";
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Response.class);
    }
    @Test
    @Order(2)
    void getStatusTestKO() {
        String process = "INVIO_PEC";
        String currStato = "BOOKED";
        String clientId = "C050";
        String nextStatus = "COMPOSED";
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(3)
    void getStatusTestKOClientId() {
        String process = "INVIO_PEC";
        String currStato = "BOOKD";
        String clientId = "null";
        String nextStatus = "COMPOSED";
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(4)
    void getStatusTestKOProccessId() {
        String process = null;
        String currStato = "BOOKED";
        String clientId = "C05";
        String nextStatus = "COMPOSED";
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    @Order(5)
    void getStatusTestKOCurrenStatus() {
        String process = "PEC";
        //String currStato = null;
        String clientId = "C05";
        String nextStatus = "COMPOSED";
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(6)
    void getStatusTestKONextStatus() {
        String process = "PEC";
        String currStato = "BOOKE";
        String clientId = "C05";
        String nextStatus = null;
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    @Order(7)
    void getStatusTestANYCurrStatus() {
        String process = "INVIO_PEC";
        String currStato = "BOOKED";
        String clientId = "C050";
        String nextStatus = "INTERNAL_ERROR";
        webClient.get()
                .uri("http://localhost:8080/statemachinemanager/validate/" +process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

}

