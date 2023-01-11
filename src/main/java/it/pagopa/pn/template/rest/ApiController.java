package it.pagopa.pn.template.rest;

import it.pagopa.pn.template.model.Response;
import it.pagopa.pn.template.model.Transaction;
import it.pagopa.pn.template.service.StateMachineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApiController  {


    @GetMapping(value="/validate/{process}/{status}")
    public Response validateStatus(@PathVariable("process") String process, @PathVariable("status") String status, @RequestParam(value = "nextStatus") String nextStatus) throws Exception{
        StateMachineService service = new StateMachineService();
        Response result = service.queryTable(process, status,nextStatus);
        System.out.println("RESULT=" + result.isAllowed());
        return result;
    }


}
