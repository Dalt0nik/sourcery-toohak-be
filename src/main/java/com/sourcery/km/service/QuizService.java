package com.sourcery.km.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizCardDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.dto.quiz.QuizRequestDto;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.exception.EntityNotFoundException;
import com.sourcery.km.exception.UnauthorizedException;
import com.sourcery.km.repository.QuestionOptionRepository;
import com.sourcery.km.repository.QuestionRepository;
import com.sourcery.km.repository.QuizRepository;
import com.sourcery.km.service.helper.QuestionHelper;
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
public class QuizService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final QuestionHelper questionHelper;

    private final QuestionOptionHelper questionOptionHelper;

    private final QuestionOptionRepository questionOptionRepository;

    private final UserService userService;

    @Transactional
    public QuizDTO createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = QuizBuilder.toQuizEntity(quizDTO);
        quiz.setCreatedBy(userService.getUserInfo().getId());
        quizRepository.insertQuiz(quiz);

//        insertQuestions(quiz);
//        insertQuestionOptions(quiz);

        questionHelper.insertQuestions(quiz);
        questionOptionHelper.insertQuestionOptions(quiz);

        return QuizBuilder.toQuizDTO(quiz);
    }

    public List<QuizCardDTO> getQuizCards() {
        return quizRepository.getQuizCardsByUserId(userService.getUserInfo().getId());
    }

    public QuizDTO getQuizById(UUID id) {
        Quiz quiz = getQuiz(id);
        return QuizBuilder.toQuizDTO(quiz);
    }

    public QuizDTO updateQuiz(QuizRequestDto quizRequestDto, UUID quizId) {
        Quiz quiz = getQuiz(quizId);
        isQuizCreator(quiz);

        quiz.setTitle(quizRequestDto.getTitle());
        quiz.setDescription(quizRequestDto.getDescription());
        quizRepository.update(quiz);
        return QuizBuilder.toQuizDTO(quiz);
    }

    @Transactional
    public void deleteQuiz(UUID quizId) {
        Quiz quiz = getQuiz(quizId);
        isQuizCreator(quiz);

//        questionOptionRepository.deleteQuestionOptionsByQuizId(quizId);
//        questionRepository.deleteQuestionsByQuizId(quizId);
        questionOptionHelper.deleteQuestionsOptionsByQuizId(quizId);
        questionHelper.deleteQuestionsByQuizId(quizId);
        quizRepository.deleteQuiz(quizId);
    }

    public Quiz getQuiz(UUID id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Quiz with id: %s does not exist", id)));
    }

    public void isQuizCreator(Quiz quiz) {
        UUID userId = userService.getUserInfo().getId();
        if (!userId.equals(quiz.getCreatedBy())) {
            throw new UnauthorizedException("User is not quiz creator");
        }
    }
//
//    private void insertQuestions(Quiz quiz) {
//        if (CollectionUtils.isNotEmpty(quiz.getQuestions())) {
//            quiz.getQuestions().forEach(question -> question.setQuizId(quiz.getId()));
//            questionRepository.insertQuestions(quiz.getQuestions());
//        }
//    }
//
//    private void insertQuestionOptions(Quiz quiz) {
//        quiz.getQuestions().forEach(question -> {
//            if (CollectionUtils.isNotEmpty(question.getQuestionOptions())) {
//                question.getQuestionOptions().forEach(option -> option.setQuestionId(question.getId()));
//                questionOptionRepository.insertQuestionOptions(question.getQuestionOptions());
//            }
//        });
//    }
}
