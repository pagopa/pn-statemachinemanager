package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.exception.StateMachineManagerException;
import it.pagopa.pn.statemachinemanager.model.ExternalStatusResponse;
import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/statemachinemanager")
public class ApiController {


    private final StateMachineService service;
    private static final String STARTING_PROCESS = "Starting {} Process";
    private static final String ENDING_PROCESS = "Ending {} Process";
    private static final String ENDING_PROCESS_WITH_ERROR = "Ending {} Process with error = {} - {}";

    public ApiController(StateMachineService service) {
        this.service = service;
    }

    @GetMapping(path = "/validate/{process}/{status}")
    public Response validateStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value =
            "clientId") String clientId, @RequestParam(value = "nextStatus") String nextStatus) {

        final String VALIDATE_STATUS = "validateStatus";
        log.info(STARTING_PROCESS, VALIDATE_STATUS);

        Response response = null;
        try{
            log.debug("Invoked queryTable with args: {} - {} - {} - {}", process, status, clientId, nextStatus);
            response = service.queryTable(process, status, clientId, nextStatus);
            log.info(ENDING_PROCESS, VALIDATE_STATUS);
        }catch(StateMachineManagerException exception){
            log.warn (ENDING_PROCESS_WITH_ERROR, VALIDATE_STATUS, exception.getClass(), exception.getMessage());
            throw exception;
        }

        return response;
    }

    @GetMapping(path = "/decodeLogical/{process}/{status}")
    public ExternalStatusResponse getExternalStatus(@PathVariable("process") String process, @PathVariable("status") String status,
                                                    @RequestParam(value = "clientId") String clientId) {

        final String GET_EXTERNAL_STATUS = "getExternalStatus";

        log.info(STARTING_PROCESS, GET_EXTERNAL_STATUS);

        ExternalStatusResponse externalStatusResponse = null;
        try{
            log.debug("Invoked getExternalStatus with args: {} - {} - {}", process, status, clientId);
            externalStatusResponse = service.getExternalStatus(process, status, clientId);
            log.info(ENDING_PROCESS, GET_EXTERNAL_STATUS);
        }catch (StateMachineManagerException exception){
            log.warn (ENDING_PROCESS_WITH_ERROR, GET_EXTERNAL_STATUS, exception.getClass(), exception.getMessage());
            throw exception;
        }

        return externalStatusResponse;
    }
}
