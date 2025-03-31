package com.sourcery.km.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.repository.QuestionOptionRepository;
import com.sourcery.km.repository.QuestionRepository;
import com.sourcery.km.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final QuestionOptionRepository questionOptionRepository;

    @Transactional
    public QuizDTO createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = QuizBuilder.toQuizEntity(quizDTO);
        quizRepository.insertQuiz(quiz);

        if (CollectionUtils.isNotEmpty(quiz.getQuestions())) {
            insertQuestions(quiz);
            insertQuestionOptions(quiz);
        }

        return QuizBuilder.toQuizDTO(quiz);
    }

    private void insertQuestions(Quiz quiz) {
        quiz.getQuestions().forEach(question -> question.setQuizId(quiz.getId()));
        questionRepository.insertQuestions(quiz.getQuestions());
    }

    private void insertQuestionOptions(Quiz quiz) {
        quiz.getQuestions().forEach(question -> {
            if (CollectionUtils.isNotEmpty(question.getQuestionOptions())) {
                question.getQuestionOptions().forEach(option -> option.setQuestionId(question.getId()));
                questionOptionRepository.insertQuestionOptions(question.getQuestionOptions());
            }
        });
    }
}
