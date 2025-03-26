package com.sourcery.km.builder.quiz;

import com.sourcery.km.builder.question.QuestionBuilder;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Quiz;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizBuilder {

    public static Quiz toQuizEntity(CreateQuizDTO quizDTO) {
        return Quiz.builder()
                .createdBy(quizDTO.getCreatedBy())
                .title(quizDTO.getTitle())
                .description(quizDTO.getDescription())
                .questions(quizDTO.getQuestions())
                .build();
    }

    public static QuizDTO toQuizDTO(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .createdBy(quiz.getCreatedBy())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .questions(QuestionBuilder.questionDTOS(quiz.getQuestions()))
                .build();
    }

}
