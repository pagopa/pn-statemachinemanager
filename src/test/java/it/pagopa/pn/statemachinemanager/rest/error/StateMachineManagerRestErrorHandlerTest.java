package it.pagopa.pn.statemachinemanager.rest.error;

import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.model.Problem;
import it.pagopa.pn.statemachinemanager.testutils.annotation.SpringBootTestWebEnv;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTestWebEnv
@AutoConfigureWebTestClient
public class StateMachineManagerRestErrorHandlerTest {

    @Test
    void handleRequestClientIdMalformedValidateClientIdTest() {
        StateMachineManagerException.ErrorRequestValidateClientId exception = new StateMachineManagerException.ErrorRequestValidateClientId("test");
        StateMachineManagerRestErrorHandler stateMachineManagerRestErrorHandler = new StateMachineManagerRestErrorHandler();
        ResponseEntity<Problem> responseEntity = stateMachineManagerRestErrorHandler.handleRequestClientIdMalformed(exception);
        assert (responseEntity.getStatusCodeValue() == 400);
    }

    @Test
    void handleRequestClientIdMalformedNotFoundClientIdTest() {
        StateMachineManagerException.ErrorRequestValidateNotFoundClientId exception = new StateMachineManagerException.ErrorRequestValidateNotFoundClientId("test");
        StateMachineManagerRestErrorHandler stateMachineManagerRestErrorHandler = new StateMachineManagerRestErrorHandler();
        ResponseEntity<Problem> responseEntity = stateMachineManagerRestErrorHandler.handleRequestClientIdMalformed(exception);
        assert (responseEntity.getStatusCodeValue() == 400);
    }

    @Test
    void handleRequestCurrentStatusMalformedTest() {
        StateMachineManagerException.ErrorRequestValidateCurrentStatus exception = new StateMachineManagerException.ErrorRequestValidateCurrentStatus("test");
        StateMachineManagerRestErrorHandler stateMachineManagerRestErrorHandler = new StateMachineManagerRestErrorHandler();
        ResponseEntity<Problem> responseEntity = stateMachineManagerRestErrorHandler.handleRequestCurrentStatusMalformed(exception);
        assert (responseEntity.getStatusCodeValue() == 400);
    }

    @Test
    void handleRequestProcessIdMalformedValidateProcessId() {
        StateMachineManagerException.ErrorRequestValidateProcessId exception = new StateMachineManagerException.ErrorRequestValidateProcessId("test");
        StateMachineManagerRestErrorHandler stateMachineManagerRestErrorHandler = new StateMachineManagerRestErrorHandler();
        ResponseEntity<Problem> responseEntity = stateMachineManagerRestErrorHandler.handleRequestProcessIdMalformed(exception);
        assert (responseEntity.getStatusCodeValue() == 400);

    }

    @Test
    void handleRequestNextStatusMalformedErrorRequestValidateNextStatus() {
        StateMachineManagerException.ErrorRequestValidateNextStatus exception = new StateMachineManagerException.ErrorRequestValidateNextStatus("test");
        StateMachineManagerRestErrorHandler stateMachineManagerRestErrorHandler = new StateMachineManagerRestErrorHandler();
        ResponseEntity<Problem> responseEntity = stateMachineManagerRestErrorHandler.handleRequestNextStatusMalformed(exception);
        assert (responseEntity.getStatusCodeValue() == 400);
    }

    @Test
    void handleRequestNextStatusMalformedNotFoundNextStatus() {
        StateMachineManagerException.ErrorRequestValidateNotFoundNextStatus exception = new StateMachineManagerException.ErrorRequestValidateNotFoundNextStatus("test");
        StateMachineManagerRestErrorHandler stateMachineManagerRestErrorHandler = new StateMachineManagerRestErrorHandler();
        ResponseEntity<Problem> responseEntity = stateMachineManagerRestErrorHandler.handleRequestNextStatusMalformed(exception);
        assert (responseEntity.getStatusCodeValue() == 404);
    }
}