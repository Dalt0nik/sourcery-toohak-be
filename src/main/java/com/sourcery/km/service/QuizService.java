package com.sourcery.km.service;

import com.sourcery.km.builder.QuizBuilder;
import com.sourcery.km.dto.CreateQuizDTO;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.mapper.QuizMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings("EI2")
public class QuizService {
    private final QuizMapper quizMapper;

    @Transactional
    public void createQuiz(CreateQuizDTO quizDTO) {
        Quiz quiz = QuizBuilder.toQuizEntity(quizDTO);
        quizMapper.insertQuiz(quiz);
    }
}
