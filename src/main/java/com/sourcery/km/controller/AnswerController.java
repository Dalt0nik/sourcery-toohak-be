package com.sourcery.km.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

    @GetMapping()
    public String getAnswer() {
        return "Hello from Spring Boot!!";
    }
}
