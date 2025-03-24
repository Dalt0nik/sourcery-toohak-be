package com.sourcery.km.controller;

import com.sourcery.km.dto.CreateQuizDTO;
import com.sourcery.km.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<CreateQuizDTO> createQuiz(@RequestBody CreateQuizDTO quizDTO) {
        quizService.createQuiz(quizDTO);
        return new ResponseEntity<>(quizDTO, HttpStatus.CREATED);
    }
}
