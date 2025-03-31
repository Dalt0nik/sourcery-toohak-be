package com.sourcery.km.controller;

import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.service.QuizService;
import com.sourcery.km.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<QuizDTO> getAllQuizzes(@AuthenticationPrincipal Jwt jwt) {
        userService.getUserInfo(jwt);
        UUID user_id = userService.getUserInfo(jwt).getId();
        return quizService.getQuizzes(user_id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public QuizDTO getQuiz(@PathVariable(value = "id") UUID id){
        return quizService.getQuizById(id);
    }
}
