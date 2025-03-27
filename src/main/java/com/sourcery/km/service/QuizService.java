package com.sourcery.km.service;

import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.mapper.QuestionMapper;
import com.sourcery.km.mapper.QuizMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizMapper quizMapper;

    private final QuestionMapper questionMapper;

    @Transactional
    public QuizDTO createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = QuizBuilder.toQuizEntity(quizDTO);
        quizMapper.insertQuiz(quiz);

        if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
            quiz.getQuestions().forEach(question -> question.setQuizId(quiz.getId()));
            questionMapper.insertQuestions(quiz.getQuestions());
        }

        return QuizBuilder.toQuizDTO(quiz);
    }
}
