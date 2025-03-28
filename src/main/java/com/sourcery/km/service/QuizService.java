package com.sourcery.km.service;

import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Quiz;
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

    @Transactional
    public QuizDTO createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = QuizBuilder.toQuizEntity(quizDTO);
        quizRepository.insertQuiz(quiz);

        if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
            quiz.getQuestions().forEach(question -> question.setQuizId(quiz.getId()));
            questionRepository.insertQuestions(quiz.getQuestions());
        }

        return QuizBuilder.toQuizDTO(quiz);
    }
}
