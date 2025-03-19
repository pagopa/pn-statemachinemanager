package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.model.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import lombok.CustomLog;
import org.springframework.web.bind.annotation.*;
import static it.pagopa.pn.statemachinemanager.constants.Constants.*;


@RestController
@CustomLog
@RequestMapping("/statemachinemanager")
public class ApiController {


    private final StateMachineService service;


    public ApiController(StateMachineService service) {
        this.service = service;
    }
    @GetMapping(path = "/validate/{process}/{status}")
    public Response validateStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value =
            "clientId") String clientId, @RequestParam(value = "nextStatus") String nextStatus) {

        log.logStartingProcess(VALIDATE_STATUS);
        Response response = null;
        try {
            response = service.queryTable(process, status, clientId, nextStatus);
        } catch (StateMachineManagerException exception) {
            log.logEndingProcess(VALIDATE_STATUS, false, exception.getMessage());
            throw exception;
        }
        log.logEndingProcess(VALIDATE_STATUS);
        return response;
    }

    @GetMapping(path = "/decodeLogical/{process}/{status}")
    public ExternalStatusResponse getExternalStatus(@PathVariable("process") String process, @PathVariable("status") String status,
                                                    @RequestParam(value = "clientId") String clientId) {


        log.logStartingProcess(GET_EXTERNAL_STATUS_PROCESS);
        ExternalStatusResponse externalStatusResponse = null;
        try {
            externalStatusResponse = service.getExternalStatus(process, status, clientId);
        } catch (StateMachineManagerException exception) {
            log.logEndingProcess(GET_EXTERNAL_STATUS, false, exception.getMessage());
            throw exception;
        }
        log.logEndingProcess(GET_EXTERNAL_STATUS);
        return externalStatusResponse;
    }
}
