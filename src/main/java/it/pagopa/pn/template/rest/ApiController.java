package it.pagopa.pn.template.rest;

import it.pagopa.pn.template.model.Response;
import it.pagopa.pn.template.service.StateMachineService;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApiController  {


    @GetMapping(value="/validate/{process}/{status}")
    public Response validateStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value = "clientId") String clientId,@RequestParam(value = "nextStatus") String nextStatus) throws Exception{
        StateMachineService service = new StateMachineService();
        Response result = service.queryTable(process, status,clientId,nextStatus);
        System.out.println("RESULT=" + result.isAllowed());
        return result;
    }


}
