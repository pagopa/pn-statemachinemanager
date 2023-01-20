//package it.pagopa.pn.statemachinemanager.rest;
//
//import it.pagopa.pn.statemachinemanager.annotation.SpringBootTestWebEnv;
//import it.pagopa.pn.statemachinemanager.model.Response;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
//
//@SpringBootTestWebEnv
//@AutoConfigureWebTestClient
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class ApiControllerTest {
//    ApiController apiController = new ApiController();
//
//
//
//
//    @Autowired
//    DynamoDbEnhancedClient dynamoDbEnhancedClient;
//
//    @Test
//    void testValidateStatusOK() throws Exception {
//
//        Response result = apiController.validateStatus("INVIO_PEC", "BOOKED", "C050","VALIDATE");
//        Response resp = new Response();
//        resp.setAllowed(true);
//        Assertions.assertEquals(resp.isAllowed() , result.isAllowed());
//    }
//
//    @Test
//    void testValidateStatusErrror() throws Exception {
//
//        Response result = apiController.validateStatus("INVIO_PEC", "BOOKED","C050", "ERROR");
//        Response resp = new Response();
//        Assertions.assertEquals(resp.isAllowed() , result.isAllowed());
//    }
//}
//
