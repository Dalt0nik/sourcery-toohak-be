package com.sourcery.km.controller;

import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes/{quizId}/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PutMapping("/{id}")
    void updateQuestion(@PathVariable(value = "quizId") UUID quizId,
                        @PathVariable(value = "id") UUID id,
                        @Valid @RequestBody(required = true) QuestionDTO questionDTO) {
        questionService.updateExistingQuestion(quizId, id, questionDTO);
    }
}
