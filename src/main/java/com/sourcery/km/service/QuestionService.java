package com.sourcery.km.service;

import com.sourcery.km.builder.question.QuestionBuilder;
import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    private final QuizService quizService;

    @Transactional
    public void insertQuestion(CreateQuestionDTO questionDTO, UUID quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        quizService.isQuizCreator(quiz);

        Question question = QuestionBuilder.toQuestionEntity(questionDTO);
        questionRepository.insertQuestion(question);
    }
}
