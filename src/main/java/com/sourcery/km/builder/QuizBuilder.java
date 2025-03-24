package com.sourcery.km.builder;

import com.sourcery.km.dto.CreateQuizDTO;
import com.sourcery.km.dto.QuizDTO;
import com.sourcery.km.entity.Quiz;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizBuilder {

    public static Quiz toQuizEntity(CreateQuizDTO quizDTO) {
        return Quiz.builder()
                .title(quizDTO.getTitle())
                .description(quizDTO.getDescription())
                .build();
    }

    public static QuizDTO toQuizDTO(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .build();
    }

}
