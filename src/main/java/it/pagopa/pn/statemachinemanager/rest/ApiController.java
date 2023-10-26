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
        try{
            log.debug(VALIDATE_STATUS_LOG, VALIDATE_STATUS,process, status, clientId, nextStatus);
            response = service.queryTable(process, status, clientId, nextStatus);
            log.logEndingProcess(VALIDATE_STATUS);
        }catch(StateMachineManagerException exception){
            log.logEndingProcess(VALIDATE_STATUS, false, exception.getMessage());
            throw exception;
        }

        return response;
    }

    @GetMapping(path = "/decodeLogical/{process}/{status}")
    public ExternalStatusResponse getExternalStatus(@PathVariable("process") String process, @PathVariable("status") String status,
                                                    @RequestParam(value = "clientId") String clientId) {


        log.logStartingProcess(GET_EXTERNAL_STATUS);

        ExternalStatusResponse externalStatusResponse = null;
        try{
            log.debug(EXTERNAL_STATUS_LOG,GET_EXTERNAL_STATUS, process, status, clientId);
            externalStatusResponse = service.getExternalStatus(process, status, clientId);
            log.logEndingProcess(GET_EXTERNAL_STATUS);
        }catch (StateMachineManagerException exception){
            log.logEndingProcess(GET_EXTERNAL_STATUS, false, exception.getMessage());
            throw exception;
        }

        return externalStatusResponse;
    }
}
