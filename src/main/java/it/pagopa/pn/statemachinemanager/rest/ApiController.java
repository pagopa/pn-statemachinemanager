package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.generated.openapi.server.v1.api.StateMachineControllerApi;
import it.pagopa.pn.statemachinemanager.generated.openapi.server.v1.dto.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.generated.openapi.server.v1.dto.ValidateStatusResponse;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class ApiController implements StateMachineControllerApi {


    private final StateMachineService service;


    public ApiController(StateMachineService service) {
        this.service = service;
    }

    @Override
    public Mono<ResponseEntity<ValidateStatusResponse>> validateStatus(String process, String status, String clientId, String nextStatus, final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> service.queryTable(process, status, clientId, nextStatus))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<ExternalStatusResponse>> getExternalStatus(String process, String status, String clientId, final ServerWebExchange exchange) {
        return Mono.fromCallable(() -> service.getExternalStatus(process, status, clientId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }
}
