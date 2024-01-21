package com.tortora.financas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GreetingServiceTest {

    @Autowired
    private GreetingService service;

    @Test
    void greetTest() {
        String response = service.greet();
        Assertions.assertEquals("Hello, World", response);
    }

}
