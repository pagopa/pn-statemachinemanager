package it.pagopa.pn.statemachinemanager.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthCheckApiController {

    @GetMapping(value = "/")
    public Mono<ResponseEntity<Void>> status() {
        return Mono.just(ResponseEntity.ok().build());
    }
}
