package it.pagopa.pn.statemachinemanager.service;

import it.pagopa.pn.statemachinemanager.annotation.SpringBootTestWebEnv;
import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.model.Transaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTestWebEnv
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StateMachineServiceTest {
    StateMachineService stateMachineService = new StateMachineService();

//    @Test
//    void testQueryTableOK() {
//        Response result = stateMachineService.queryTable("INVIO_PEC", "BOOKED", "C050","VALIDATE");
//        Response resp = new Response();
//        resp.setAllowed(true);
//
//        Assertions.assertEquals(resp.isAllowed(), result.isAllowed());
//    }
//    @Test
//    void testQueryTableError() {
//        Response result = stateMachineService.queryTable("INVIO_PEC", "BOOKED","C050", "ERROR");
//        Response resp = new Response();
//
//
//        Assertions.assertEquals(resp.isAllowed(), result.isAllowed());
//    }

    @Autowired
    private WebTestClient webClient;

    //ECGRAC.100.1
    @Test
    @Order(1)
    void insertTransactionTest() {

        Transaction transaction = new Transaction();
        ArrayList<String> listArray = new ArrayList<>();
        listArray.add("VALIDATE");
        transaction.setProcessClientId("INVIO_PEC#C050");
        transaction.setCurrStatus("BOOKED");
        transaction.setTargetStatus(listArray);


        webClient.post()
                .uri("http://localhost:8080/")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(transaction))
                .exchange()
                .expectStatus()
                .isOk();
    }
    @Test
    @Order(2)
    void getStatusTest() {
        String process = "INVIO_PEC";
        String currStato = "BOOKED";
        String clientId = "C050";
        String nextStatus = "VALIDATE";
        webClient.get()
                .uri("http://localhost:8080/validate/"+process +"/"+ currStato +"?clientId="+clientId + "&nextStatus="+ nextStatus)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Response.class);
    }

}

