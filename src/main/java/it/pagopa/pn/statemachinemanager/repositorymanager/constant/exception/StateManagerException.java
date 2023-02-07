package it.pagopa.pn.statemachinemanager.repositorymanager.constant.exception;

public class StateManagerException extends RuntimeException{


    public StateManagerException() {
        super("Generic exception in StateManagerService");
    }


    public static class ErrorRequestValidate extends RuntimeException {

        public ErrorRequestValidate(String message) {
            super(String.format("Errore validazione dati clientId : '%s'", message));
        }
    }

}
