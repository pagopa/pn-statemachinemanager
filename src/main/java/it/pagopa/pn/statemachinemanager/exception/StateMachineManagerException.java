package it.pagopa.pn.statemachinemanager.exception;

public class StateMachineManagerException extends RuntimeException {


    public StateMachineManagerException() {
        super("Generic exception in StateManagerService");
    }


    public static class ErrorRequestValidateClientId extends RuntimeException {

        public ErrorRequestValidateClientId(String message) {
            super(String.format("Errore validazione dati clientId : '%s'", message));
        }
    }

    public static class ErrorRequestValidateNotFoundClientId extends RuntimeException {

        public ErrorRequestValidateNotFoundClientId(String message) {
            super(String.format("Not Found clientId : '%s'", message));
        }
    }

    public static class ErrorRequestValidateProcessId extends RuntimeException {

        public ErrorRequestValidateProcessId(String message) {
            super(String.format("Errore validazione dati processId : '%s'", message));
        }
    }

    public static class ErrorRequestValidateCurrentStatus extends RuntimeException {

        public ErrorRequestValidateCurrentStatus(String message) {
            super(String.format("Errore validazione dati currentStatus : '%s'", message));
        }
    }

    public static class ErrorRequestValidateNotFoundCurrentStatus extends RuntimeException {

        public ErrorRequestValidateNotFoundCurrentStatus(String message) {
            super(String.format("NotFound currentStatus : '%s'", message));
        }
    }

    public static class ErrorRequestValidateNextStatus extends RuntimeException {

        public ErrorRequestValidateNextStatus(String message) {
            super(String.format("Errore validazione dati nextStatus : '%s'", message));
        }
    }

    public static class ErrorRequestValidateNotFoundNextStatus extends RuntimeException {

        public ErrorRequestValidateNotFoundNextStatus(String message) {
            super(String.format("Not Found  nextStatus : '%s'", message));
        }
    }

}
