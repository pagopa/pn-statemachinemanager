package it.pagopa.pn.statemachinemanager.repositorymanager.constant.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(StateManagerException.ErrorRequestValidateClientId.class)
    public final ResponseEntity<Problem> handleRequestClientIdMalformed(StateManagerException.ErrorRequestValidateClientId exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateManagerException.ErrorRequestValidateNotFoundClientId.class)
    public final ResponseEntity<Problem> handleRequestClientIdMalformed(StateManagerException.ErrorRequestValidateNotFoundClientId exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateManagerException.ErrorRequestValidateCurrentStatus.class)
    public final ResponseEntity<Problem> handleRequestCurrentStatusMalformed(StateManagerException.ErrorRequestValidateCurrentStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }
    @ExceptionHandler(StateManagerException.ErrorRequestValidateNotFoundCurrentStatus.class)
    public final ResponseEntity<Problem> handleRequestCurrentStatusMalformed(StateManagerException.ErrorRequestValidateNotFoundCurrentStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, NOT_FOUND);
    }
    @ExceptionHandler(StateManagerException.ErrorRequestValidateProccesId.class)
    public final ResponseEntity<Problem> handleRequestProccesIdMalformed(StateManagerException.ErrorRequestValidateProccesId exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateManagerException.ErrorRequestValidateNextStatus.class)
    public final ResponseEntity<Problem> handleRequestNextStatusMalformed(StateManagerException.ErrorRequestValidateNextStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler(StateManagerException.ErrorRequestValidateNotFoundNextStatus.class)
    public final ResponseEntity<Problem> handleRequestNextStatusMalformed(StateManagerException.ErrorRequestValidateNotFoundNextStatus exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, NOT_FOUND);
    }

}
