package it.pagopa.pn.statemachinemanager.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
public class ExternalStatusResponse {

    String externalStatus;
    String logicStatus;
}
