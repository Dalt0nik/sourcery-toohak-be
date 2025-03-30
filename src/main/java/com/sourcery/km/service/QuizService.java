package com.sourcery.km.service;

import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.exception.QuizNotFoundException;
import com.sourcery.km.repository.QuestionRepository;
import com.sourcery.km.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    private Quiz getQuiz(UUID id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(String.format("Quiz with id: %s does not exist" , id)));
    }

    public QuizDTO getQuizById(UUID id) {
        Quiz quiz = getQuiz(id);
        return QuizBuilder.toQuizDTO(quiz);
    }


}
