package com.sourcery.km.builder.quiz;

import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.dto.quiz.CreateQuizDTO;
import com.sourcery.km.dto.quiz.QuizDTO;
import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.Quiz;
import com.sourcery.km.service.MapperService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class QuizBuilder {

    @Autowired
    private final MapperService mapperService;

    public Quiz toQuizEntity(CreateQuizDTO quizDTO) {
        return Quiz.builder()
                .title(quizDTO.getTitle())
                .description(quizDTO.getDescription())
                .coverImageId(quizDTO.getImageId())
                .questions(mapperService.mapList(quizDTO.getQuestions(), Question.class))
                .build();
    }

    public QuizDTO toQuizDTO(Quiz quiz) {
        return QuizDTO.builder()
                .id(quiz.getId())
                .createdBy(quiz.getCreatedBy())
                .imageId(quiz.getCoverImageId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .createdAt(quiz.getCreatedAt())
                .updatedAt(quiz.getUpdatedAt())
                .questions(mapperService.mapList(quiz.getQuestions(), QuestionDTO.class))
                .build();
    }

}
