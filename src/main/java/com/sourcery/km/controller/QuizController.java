package com.sourcery.km.controller;

import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizCardDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.dto.quiz.QuizRequestDto;
import com.sourcery.km.service.QuizService;
import com.sourcery.km.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public QuizDTO createQuiz(@Valid @RequestBody CreateQuizDTO createQuizDTO) {
        return quizService.createQuiz(createQuizDTO);
    }

    @GetMapping
    public List<QuizCardDTO> getAllQuizCards() {
        return quizService.getQuizCards();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public QuizDTO getQuiz(@PathVariable(value = "id") UUID id) {
        return quizService.getQuizById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public QuizDTO updateQuiz (@PathVariable(value = "id") UUID id,
                               @Valid @RequestBody QuizRequestDto requestDto) {
        return quizService.updateQuiz(requestDto, id);
    }
}
