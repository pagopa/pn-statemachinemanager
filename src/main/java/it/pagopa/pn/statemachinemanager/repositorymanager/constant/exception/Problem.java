package it.pagopa.pn.statemachinemanager.repositorymanager.constant.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;



@Data
public class Problem {

    @JsonProperty("detail")
    private String detail;


}
