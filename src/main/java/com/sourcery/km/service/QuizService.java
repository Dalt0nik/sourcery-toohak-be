package com.sourcery.km.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.sourcery.km.builder.file.FileBuilder;
import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizCardDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.dto.quiz.QuizRequestDto;
import com.sourcery.km.entity.File;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.exception.EntityNotFoundException;
import com.sourcery.km.exception.UnauthorizedException;
import com.sourcery.km.repository.FileRepository;
import com.sourcery.km.repository.QuestionOptionRepository;
import com.sourcery.km.repository.QuestionRepository;
import com.sourcery.km.repository.QuizRepository;
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

    private final QuestionOptionRepository questionOptionRepository;

    private final FileRepository fileRepository;

    private final UserService userService;

    @Transactional
    public QuizDTO createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = QuizBuilder.toQuizEntity(quizDTO);
        quiz.setCreatedBy(userService.getUserInfo().getId());
        if (quizDTO.getImageId() != null) {
            File file = FileBuilder.fromFileIdSetTemporary(quizDTO.getImageId(), false);
            fileRepository.updateFile(file);
        }
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
                .orElseThrow(() -> new EntityNotFoundException(String.format("Quiz with id: %s does not exist", id)));
    }

    private boolean isQuizCreator(Quiz quiz) {
        UUID userId = userService.getUserInfo().getId();
        return userId.equals(quiz.getCreatedBy());
    }

    public QuizDTO getQuizById(UUID id) {
        Quiz quiz = getQuiz(id);
        return QuizBuilder.toQuizDTO(quiz);
    }

    public List<QuizCardDTO> getQuizCards() {
        return quizRepository.getQuizCardsByUserId(userService.getUserInfo().getId());
    }

    public QuizDTO updateQuiz(QuizRequestDto quizRequestDto, UUID quizId) {
        Quiz quiz = getQuiz(quizId);
        if (isQuizCreator(quiz)) {
            quiz.setTitle(quizRequestDto.getTitle());
            quiz.setDescription(quizRequestDto.getDescription());
            quizRepository.update(quiz);
            return QuizBuilder.toQuizDTO(quiz);
        } else {
            throw new UnauthorizedException("user is not quiz creator");
        }
    }

    @Transactional
    public void deleteQuiz(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Quiz with id: %s does not exist", quizId)));

        if (!isQuizCreator(quiz)) {
            throw new UnauthorizedException("User is not quiz creator");
        }

        questionOptionRepository.deleteQuestionOptionsByQuizId(quizId);
        questionRepository.deleteQuestionsByQuizId(quizId);
        quizRepository.deleteQuiz(quizId);
    }
}
