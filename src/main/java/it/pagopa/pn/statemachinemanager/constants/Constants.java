package it.pagopa.pn.statemachinemanager.constants;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

public class Constants {
    public static final DefaultCredentialsProvider DEFAULT_CREDENTIALS_PROVIDER = DefaultCredentialsProvider.create();
    public static final String VALIDATE_STATUS = "validateStatus";
    public static final String GET_EXTERNAL_STATUS = "getExternalStatus";
    public static final String VALIDATE_STATUS_LOG = "Invocation of {} with args: {} - {} - {} - {}";
    public static final String EXTERNAL_STATUS_LOG = "Invocation of {} with args: {} - {} - {}";

    private Constants(){
        super();
    }



}
