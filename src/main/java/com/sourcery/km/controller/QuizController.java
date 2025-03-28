package com.sourcery.km.controller;

import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public QuizDTO createQuiz(@Valid @RequestBody CreateQuizDTO createQuizDTO) {
        return quizService.createQuiz(createQuizDTO);
    }
}
