package it.pagopa.pn.statemachinemanager.repositorymanager.constant.model;

public class ExternalStatusResponse {
    private String externalStatus;
    private String logicStatus;

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
}
