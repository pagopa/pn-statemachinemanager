package it.pagopa.pn.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TemplateApplication {
    private static final Logger logger = LoggerFactory.getLogger(TemplateApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

}