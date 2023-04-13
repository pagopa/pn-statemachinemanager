package it.pagopa.pn.statemachinemanager.rest.error;


import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.model.Problem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class StateMachineManagerRestErrorHandler {

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateClientId.class)
    public final ResponseEntity<Problem> handleRequestClientIdMalformed(StateMachineManagerException.ErrorRequestValidateClientId exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateNotFoundClientId.class)
    public final ResponseEntity<Problem> handleRequestClientIdMalformed(StateMachineManagerException.ErrorRequestValidateNotFoundClientId exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateCurrentStatus.class)
    public final ResponseEntity<Problem> handleRequestCurrentStatusMalformed(StateMachineManagerException.ErrorRequestValidateCurrentStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateNotFoundCurrentStatus.class)
    public final ResponseEntity<Problem> handleRequestCurrentStatusMalformed(StateMachineManagerException.ErrorRequestValidateNotFoundCurrentStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, NOT_FOUND);
    }

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateProcessId.class)
    public final ResponseEntity<Problem> handleRequestProcessIdMalformed(StateMachineManagerException.ErrorRequestValidateProcessId exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateNextStatus.class)
    public final ResponseEntity<Problem> handleRequestNextStatusMalformed(StateMachineManagerException.ErrorRequestValidateNextStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateMachineManagerException.ErrorRequestValidateNotFoundNextStatus.class)
    public final ResponseEntity<Problem> handleRequestNextStatusMalformed(StateMachineManagerException.ErrorRequestValidateNotFoundNextStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, NOT_FOUND);
    }
}
