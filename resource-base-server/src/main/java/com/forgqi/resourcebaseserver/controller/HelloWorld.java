package com.forgqi.resourcebaseserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @Value("${spring.application.name}")
    String server;

    @RequestMapping("/")
    public String home(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + " ,i am from server:" + server;
    }
}
