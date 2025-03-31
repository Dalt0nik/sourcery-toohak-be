package com.sourcery.km.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.repository.QuestionOptionRepository;
import com.sourcery.km.exception.QuizNotFoundException;
import com.sourcery.km.repository.QuestionRepository;
import com.sourcery.km.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    private Quiz getQuiz(UUID id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(String.format("Quiz with id: %s does not exist" , id)));
    }

    public QuizDTO getQuizById(UUID id) {
        Quiz quiz = getQuiz(id);
        return QuizBuilder.toQuizDTO(quiz);
    }

    public List<QuizDTO> getQuizzes(String sub) {

        List<Quiz> quizzes = quizRepository.getQuizzesByAuth0Id(sub);

        for (Quiz quiz : quizzes) {
            var questions = questionRepository.getQuestionsByQuizId(quiz.getId());
            quiz.setQuestions(questions);
        }

        return quizzes.stream()
                .map(QuizBuilder::toQuizDTO)
                .toList();
    }


}
