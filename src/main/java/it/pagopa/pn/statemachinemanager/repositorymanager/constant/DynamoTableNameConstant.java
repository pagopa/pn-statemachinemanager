package it.pagopa.pn.statemachinemanager.repositorymanager.constant;

public final class DynamoTableNameConstant {

    private DynamoTableNameConstant() {
        throw new IllegalStateException("DynamoTableNameConstant is a class of constant");
    }

    public static final String TRANSACTION_TABLE_NAME = "Transaction";
}
