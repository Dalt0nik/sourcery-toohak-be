package com.sourcery.km.builder.questionOption;

import com.sourcery.km.dto.questionOption.CreateQuestionOptionDTO;
import com.sourcery.km.dto.questionOption.QuestionOptionDTO;
import com.sourcery.km.entity.QuestionOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionOptionBuilder {

    public static QuestionOption toQuestionOptionEntity(CreateQuestionOptionDTO questionOptionDTO) {
        return QuestionOption.builder()
                .questionId(questionOptionDTO.getQuestionId())
                .title(questionOptionDTO.getTitle())
                .ordering(questionOptionDTO.getOrdering())
                .isCorrect(questionOptionDTO.getIsCorrect())
                .build();
    }

    public static QuestionOption toQuestionOptionEntity(QuestionOptionDTO questionOptionDTO) {
        return QuestionOption.builder()
                .id(questionOptionDTO.getId())
                .questionId(questionOptionDTO.getQuestionId())
                .title(questionOptionDTO.getTitle())
                .ordering(questionOptionDTO.getOrdering())
                .isCorrect(questionOptionDTO.getIsCorrect())
                .build();
    }

    public static QuestionOptionDTO toQuestionOptionDTO(QuestionOption questionOption) {
        return QuestionOptionDTO.builder()
                .id(questionOption.getId())
                .questionId(questionOption.getQuestionId())
                .title(questionOption.getTitle())
                .ordering(questionOption.getOrdering())
                .isCorrect(questionOption.getIsCorrect())
                .build();
    }

    public static List<QuestionOption> toQuestionOptionEntities(List<CreateQuestionOptionDTO> questionOptions) {
        if (questionOptions == null) {
            return null;
        }
        return questionOptions.stream()
                .map(QuestionOptionBuilder::toQuestionOptionEntity)
                .toList();
    }

    public static List<QuestionOption> toQuestionOptionsEntities(List<QuestionOptionDTO> questionOptions) {
        if (questionOptions == null) {
            return null;
        }
        return questionOptions.stream()
                .map(QuestionOptionBuilder::toQuestionOptionEntity)
                .toList();
    }

    public static List<QuestionOptionDTO> toQuestionOptionDTOS(List<QuestionOption> questionOptions) {
        if (questionOptions == null) {
            return null;
        }
        return questionOptions.stream()
                .map(QuestionOptionBuilder::toQuestionOptionDTO)
                .toList();
    }
}
