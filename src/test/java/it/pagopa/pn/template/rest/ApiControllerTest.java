package it.pagopa.pn.template.rest;

import it.pagopa.pn.template.model.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

class ApiControllerTest {
    ApiController apiController = new ApiController();




    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Test
    void testValidateStatusOK() throws Exception {

        Response result = apiController.validateStatus("INVIO_PEC#C050", "BOOKED", "VALIDATE");
        Response resp = new Response();
        resp.setAllowed(true);
        Assertions.assertEquals(resp.isAllowed() , result.isAllowed());
    }

    @Test
    void testValidateStatusErrror() throws Exception {

        Response result = apiController.validateStatus("INVIO_PEC#C050", "BOOKED", "ERROR");
        Response resp = new Response();
        Assertions.assertEquals(resp.isAllowed() , result.isAllowed());
    }
}

