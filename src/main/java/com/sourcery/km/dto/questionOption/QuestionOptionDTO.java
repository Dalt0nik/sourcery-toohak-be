package com.sourcery.km.dto.questionOption;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Value
@Builder
public class QuestionOptionDTO {
    UUID id;

    UUID questionId;

    String title;

    Integer ordering;

    Boolean isCorrect;
}
