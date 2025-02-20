package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class AnswerController {

  @GetMapping("/api")
  public String getAnswer() {
    return "Hello from Spring Boot!!";
  }
}
