package com.sourcery.km.service.helper;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuestionHelper {
    private final QuestionRepository questionRepository;

    public void insertQuestions(Quiz quiz) {
        if (CollectionUtils.isNotEmpty(quiz.getQuestions())) {
            quiz.getQuestions().forEach(question -> question.setQuizId(quiz.getId()));
            questionRepository.insertQuestions(quiz.getQuestions());
        }
    }

    public void deleteQuestionsByQuizId(UUID quizId) {
        questionRepository.deleteQuestionsByQuizId(quizId);
    }
}
