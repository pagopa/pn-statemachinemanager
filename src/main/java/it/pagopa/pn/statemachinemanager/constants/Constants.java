package it.pagopa.pn.statemachinemanager.constants;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

public class Constants {
    public static final DefaultCredentialsProvider DEFAULT_CREDENTIALS_PROVIDER = DefaultCredentialsProvider.create();
    public static final String VALIDATE_STATUS = "validateStatus";
    public static final String QUERY_TABLE = "StateMachineService.queryTable()";
    public static final String GET_EXTERNAL_STATUS_PROCESS = "getExternalStatus";
    public static final String GET_EXTERNAL_STATUS = "StateMachineService.getExternalStatus()";
    public static final String INVOKING_OPERATION_LABEL_WITH_ARGS = "Invoking operation '{}' with args: {}";

    private Constants(){
        super();
    }



}
