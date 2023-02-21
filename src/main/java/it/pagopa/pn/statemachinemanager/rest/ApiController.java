package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.repositorymanager.constant.model.Response;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/statemachinemanager")
public class ApiController  {


    private final StateMachineService service;

    public ApiController(StateMachineService service) {
        this.service = service;
    }

    @GetMapping(path="/validate/{process}/{status}")
    public Response validateStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value = "clientId") String clientId,@RequestParam(value = "nextStatus") String nextStatus) {
        return service.queryTable(process, status,clientId,nextStatus);
    }

    @GetMapping(path="/decodeLogical/{process}/{status}")
    public ExternalStatusResponse getExternalStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value = "clientId") String clientId){
        return service.getExternalStatus(process, status, clientId);
    }


}
