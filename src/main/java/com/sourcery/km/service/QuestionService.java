package com.sourcery.km.service;

import com.sourcery.km.builder.question.QuestionBuilder;
import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.dto.question.QuestionDTO;
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

    private final MapperService mapperService;

    @Transactional
    public void insertQuestion(CreateQuestionDTO questionDTO, UUID quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        quizService.isQuizCreator(quiz);

        Question question = mapperService.map(questionDTO, Question.class);
        quiz.setQuestions(List.of(question));
        questionRepository.insertQuestion(question);
        questionOptionHelper.insertQuestionOptions(quiz);
    }

    // this has to be able to update the title and the questions
    @Transactional
    public void updateExistingQuestion(UUID quizId, UUID questionId, QuestionDTO questionDto) {
        Quiz quiz = quizService.getQuiz(quizId);
        quizService.isQuizCreator(quiz);

        Question question = mapperService.map(questionDto, Question.class);
        question.setId(questionId);

        List<QuestionOption> questionOptions = question.getQuestionOptions();

        if (!questionOptions.isEmpty()) {
            questionOptions.forEach(questionOptionRepository::updateQuestionOption);
        }
        questionRepository.updateExistingQuestion(question);
    }

    public List<QuestionDTO> getQuestionsByQuizId (UUID quizId) {
        List<Question> questions = questionRepository.getQuestionsByQuizId(quizId);
        return mapperService.mapList(questions, QuestionDTO.class);
    }
}
