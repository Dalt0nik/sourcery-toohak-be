package com.sourcery.km.controller;

import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.dto.quiz.QuizRequestDto;
import com.sourcery.km.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public QuizDTO getQuiz(@PathVariable(value = "id") UUID id){
        return quizService.getQuizById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public QuizDTO updateQuiz (@PathVariable(value = "id") UUID id,
                               @Valid @RequestBody QuizRequestDto requestDto){
        return quizService.updateQuiz(requestDto, id);
    }
}
