package it.pagopa.pn.template.service;

import it.pagopa.pn.template.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StateMachineServiceTest {
    StateMachineService stateMachineService = new StateMachineService();

    @Test
    void testQueryTableOK() {
        Response result = stateMachineService.queryTable("INVIO_PEC#C050", "BOOKED", "VALIDATE");
        Response resp = new Response();
        resp.setAllowed(true);

        Assertions.assertEquals(resp.isAllowed(), result.isAllowed());
    }
    @Test
    void testQueryTableError() {
        Response result = stateMachineService.queryTable("INVIO_PEC#C050", "BOOKED", "ERROR");
        Response resp = new Response();


        Assertions.assertEquals(resp.isAllowed(), result.isAllowed());
    }
}

