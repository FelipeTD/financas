package com.tortora.financas.controller;

import com.tortora.financas.service.GreetingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @RequestMapping("/greeting")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String greeting() {
        return greetingService.greet();
    }

}
