package it.pagopa.pn.statemachinemanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Problem {

    @JsonProperty("detail")
    String detail;
}
