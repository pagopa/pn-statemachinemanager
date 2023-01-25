package it.pagopa.pn.statemachinemanager.rest;

import it.pagopa.pn.statemachinemanager.model.Response;
import it.pagopa.pn.statemachinemanager.service.StateMachineService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/statemachinemanager")
public class ApiController  {


    private final StateMachineService service;

    public ApiController(StateMachineService service) {
        this.service = service;
    }

    @GetMapping(path="/validate/{process}/{status}")
    public Response validateStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value = "clientId") String clientId,@RequestParam(value = "nextStatus") String nextStatus) throws Exception{
        Response result = service.queryTable(process, status,clientId,nextStatus);
        System.out.println("RESULT=" + result.isAllowed());
        return result;
    }


}
