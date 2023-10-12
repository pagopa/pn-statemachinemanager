package it.pagopa.pn.statemachinemanager.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureWebTestClient
class StateMachineManagerExceptionTest {


    @Test
    void testStateMachineManagerException() {
        StateMachineManagerException ex = new StateMachineManagerException();
        Assertions.assertEquals("Generic exception in StateManagerService", ex.getMessage());
    }

    @Test
    void testErrorRequestValidateClientId() {
        String message = "TestClientId";
        StateMachineManagerException.ErrorRequestValidateClientId ex =
                new StateMachineManagerException.ErrorRequestValidateClientId(message);
        Assertions.assertEquals(String.format("Errore validazione dati clientId : '%s'", message), ex.getMessage());
    }

    @Test
    void testErrorRequestValidateNotFoundClientId() {
        String message = "TestClientId";
        StateMachineManagerException.ErrorRequestValidateNotFoundClientId ex =
                new StateMachineManagerException.ErrorRequestValidateNotFoundClientId(message);
        Assertions.assertEquals(String.format("Not Found clientId : '%s'", message), ex.getMessage());
    }

    @Test
    void testErrorRequestValidateProcessId() {
        String message = "TestProcessId";
        StateMachineManagerException.ErrorRequestValidateProcessId ex =
                new StateMachineManagerException.ErrorRequestValidateProcessId(message);
        Assertions.assertEquals(String.format("Errore validazione dati processId : '%s'", message), ex.getMessage());
    }

    @Test
    void testErrorRequestValidateCurrentStatus(){
        String message = "TestValidateCurrentStatus";
        StateMachineManagerException.ErrorRequestValidateCurrentStatus ex =
                new StateMachineManagerException.ErrorRequestValidateCurrentStatus(message);
        Assertions.assertEquals(String.format("Errore validazione dati currentStatus : '%s'", message), ex.getMessage());
    }

    @Test
    void testErrorRequestValidateNextStatus(){
        String message = "TestValidateNextStatus";
        StateMachineManagerException.ErrorRequestValidateNextStatus ex =
                new StateMachineManagerException.ErrorRequestValidateNextStatus(message);
        Assertions.assertEquals(String.format("Errore validazione dati nextStatus : '%s'", message), ex.getMessage());
    }




}