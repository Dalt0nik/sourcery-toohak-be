package com.sourcery.km.controller;

import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionContoller {

    private final QuestionService questionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createQuestion(@Valid @RequestBody CreateQuestionDTO questionDTO) {
        questionService.insertQuestion(questionDTO);
    }
}
