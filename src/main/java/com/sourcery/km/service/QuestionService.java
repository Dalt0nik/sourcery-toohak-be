package com.sourcery.km.service;

import com.sourcery.km.builder.question.QuestionBuilder;
import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.QuestionOption;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.exception.InvalidPayload;
import com.sourcery.km.exception.UnauthorizedException;
import com.sourcery.km.repository.QuestionOptionRepository;
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
    private final QuizService quizService;

    private final QuestionRepository questionRepository;

    private final QuestionOptionRepository questionOptionRepository;

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

    // this has to be able to update the title and the questions
    @Transactional
    public void updateExistingQuestion(UUID quizId, UUID questionId, QuestionDTO questionDto) {
        Quiz quiz = quizService.getQuiz(quizId);
        if (!quizService.isQuizCreator(quiz))
            throw new UnauthorizedException("User not authorized to modify question");

        Question question = QuestionBuilder.toQuestionEntity(questionDto);
        question.setId(questionId);

        List<QuestionOption> questionOptions = question.getQuestionOptions();

        try {
            if (!questionOptions.isEmpty()) {
                questionOptions.forEach(questionOptionRepository::updateQuestionOption);
            }
            questionRepository.updateExistingQuestion(question);
        } catch (RuntimeException e) {
            throw new InvalidPayload("Either question attributes or question options are missing");
        }
    }
}
