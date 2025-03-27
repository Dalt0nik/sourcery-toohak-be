package com.sourcery.km.builder.question;

import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.entity.Question;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionBuilder {

    public static Question toQuestionEntity(CreateQuestionDTO questionDTO) {
        return Question.builder()
                .title(questionDTO.getTitle())
                .build();
    }

    public static QuestionDTO toQuestionDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .quizId(question.getQuizId())
                .imageId(question.getImageId())
                .title(question.getTitle())
                .build();
    }

    public static List<QuestionDTO> toQuestionDTOS(List<Question> questions) {
        if (questions == null) {
            return null;
        }
        return questions.stream()
                .map(QuestionBuilder::toQuestionDTO)
                .toList();
    }

    public static List<Question> toQuestionEntities(List<CreateQuestionDTO> questions) {
        if (questions == null) {
            return null;
        }
        return questions.stream()
                .map(QuestionBuilder::toQuestionEntity)
                .toList();
    }
}
