package it.pagopa.pn.statemachinemanager.repositorymanager.constant.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(StateManagerException.class)
    public final ResponseEntity<Problem> handleRequestMalformed(StateManagerException.ErrorRequestValidate exception) {
        var problem = new Problem();
        problem.setDetail(exception.getMessage());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

}
