package it.pagopa.pn.statemachinemanager.repositorymanager.constant.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
public class Transaction {

    private String processClientId;
    private String currStatus;
    private List<String> targetStatus;

    private String externalStatus;
    private String logicStatus;


    @DynamoDbPartitionKey
    public String getProcessClientId() {
        return processClientId;
    }

    public void setProcessClientId(String processClientId) {
        this.processClientId = processClientId;
    }

    @DynamoDbSortKey
    public String getCurrStatus() {
        return currStatus;
    }

    public void setCurrStatus(String currStatus) {
        this.currStatus = currStatus;
    }

    public List<String> getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(List<String> targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getExternalStatus() {
        return externalStatus;
    }

    public void setExternalStatus(String externalStatus) {
        this.externalStatus = externalStatus;
    }

    public String getLogicStatus() {
        return logicStatus;
    }

    public void setLogicStatus(String logicStatus) {
        this.logicStatus = logicStatus;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "processClientId='" + processClientId + '\'' +
                ", currStatus='" + currStatus + '\'' +
                ", targetStatus='" + targetStatus + '\'' +
                '}';
    }
}
