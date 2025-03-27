package com.sourcery.km.dto.question;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateQuestionDTO {
    UUID quizId;

    UUID imageId;

    String title;


}
