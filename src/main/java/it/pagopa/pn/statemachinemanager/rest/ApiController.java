package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.generated.openapi.server.v1.api.StateMachineControllerApi;
import it.pagopa.pn.statemachinemanager.generated.openapi.server.v1.dto.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.generated.openapi.server.v1.dto.ValidateStatusResponse;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import lombok.CustomLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static it.pagopa.pn.statemachinemanager.constants.Constants.*;

@RestController
@CustomLog
public class ApiController implements StateMachineControllerApi {


    private final StateMachineService service;


    public ApiController(StateMachineService service) {
        this.service = service;
    }

    @Override
    public Mono<ResponseEntity<ValidateStatusResponse>> validateStatus(String process, String status, String clientId, String nextStatus, final ServerWebExchange exchange) {

        log.logStartingProcess(VALIDATE_STATUS);
        return Mono.fromCallable(() -> service.queryTable(process, status, clientId, nextStatus))
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.logEndingProcess(VALIDATE_STATUS))
                .doOnError(StateMachineManagerException.class, exception -> log.logEndingProcess(VALIDATE_STATUS, false, exception.getMessage(), exception));
    }

    @Override
    public Mono<ResponseEntity<ExternalStatusResponse>> getExternalStatus(String process, String status, String clientId, final ServerWebExchange exchange) {

        log.logStartingProcess(GET_EXTERNAL_STATUS_PROCESS);
        return Mono.fromCallable(() -> service.getExternalStatus(process, status, clientId))
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.logEndingProcess(GET_EXTERNAL_STATUS))
                .doOnError(StateMachineManagerException.class, exception -> log.logEndingProcess(GET_EXTERNAL_STATUS, false, exception.getMessage(), exception));
    }
}
