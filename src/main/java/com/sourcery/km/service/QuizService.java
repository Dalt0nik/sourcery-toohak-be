package com.sourcery.km.service;

import com.sourcery.km.builder.file.FileBuilder;
import com.sourcery.km.builder.quiz.QuizBuilder;
import com.sourcery.km.dto.quiz.*;
import com.sourcery.km.entity.File;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.exception.EntityNotFoundException;
import com.sourcery.km.exception.ForbiddenException;
import com.sourcery.km.repository.FileRepository;
import com.sourcery.km.repository.QuizRepository;
import com.sourcery.km.service.helper.QuestionHelper;
import com.sourcery.km.service.helper.QuestionOptionHelper;
import com.sourcery.km.service.helper.QuizMapper;
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

    private final QuestionHelper questionHelper;

    private final QuestionOptionHelper questionOptionHelper;

    private final FileRepository fileRepository;

    private final UserService userService;

    private final MapperService mapperService;

    private final QuizMapper quizMapper;

    @Transactional
    public QuizDTO createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = mapperService.map(quizDTO, Quiz.class);
        quiz.setCreatedBy(userService.getUserInfo().getId());
        if (quizDTO.getImageId() != null) {
            File file = FileBuilder.fromFileIdSetTemporary(quizDTO.getImageId(), false);
            fileRepository.updateFile(file);
        }
        quizRepository.insertQuiz(quiz);

        questionHelper.insertQuestions(quiz);
        questionOptionHelper.insertQuestionOptions(quiz);

        return mapperService.map(quiz, QuizDTO.class);
    }

    public List<QuizCardDTO> getQuizCards() {
        return quizRepository.getQuizCardsByUserId(userService.getUserInfo().getId());
    }

    public QuizDTO getQuizById(UUID id) {
        List<QuizFlatRow> flatRow = quizRepository.findQuizById(id);
        if (flatRow.isEmpty()) {
            throw new EntityNotFoundException(String.format("Quiz with id: %s does not exist", id));
        }
        return quizMapper.toQuizDto(flatRow);
    }

    public QuizDTO updateQuiz(QuizRequestDto quizRequestDto, UUID quizId) {
        Quiz quiz = getQuiz(quizId);
        isQuizCreator(quiz);

        quiz.setTitle(quizRequestDto.getTitle());
        quiz.setDescription(quizRequestDto.getDescription());
        quizRepository.update(quiz);
        return mapperService.map(quiz, QuizDTO.class);
    }

    @Transactional
    public void deleteQuiz(UUID quizId) {
        Quiz quiz = getQuiz(quizId);
        isQuizCreator(quiz);

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
            throw new ForbiddenException("User is not quiz creator");
        }
    }
}
