package com.sourcery.km.service;

import com.sourcery.km.builder.question.QuestionBuilder;
import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.repository.QuestionRepository;
import com.sourcery.km.service.helper.QuestionOptionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    private final QuizService quizService;

    private final QuestionOptionHelper questionOptionHelper;

    @Transactional
    public void insertQuestion(CreateQuestionDTO questionDTO, UUID quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        quizService.isQuizCreator(quiz);

        Question question = QuestionBuilder.toQuestionEntity(questionDTO);
        quiz.setQuestions(List.of(question));
        questionRepository.insertQuestion(question);
        questionOptionHelper.insertQuestionOptions(quiz);
    }
}
