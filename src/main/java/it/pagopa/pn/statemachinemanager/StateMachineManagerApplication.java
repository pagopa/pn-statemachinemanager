package it.pagopa.pn.statemachinemanager;

import it.pagopa.pn.commons.configs.listeners.TaskIdApplicationListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StateMachineManagerApplication {

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(StateMachineManagerApplication.class);
        app.addListeners(new TaskIdApplicationListener());
        app.run(args);
    }
}
