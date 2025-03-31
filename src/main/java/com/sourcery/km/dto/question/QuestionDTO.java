package com.sourcery.km.dto.question;

import com.sourcery.km.dto.questionOption.QuestionOptionDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class QuestionDTO {
    UUID id;

    UUID quizId;

    UUID imageId;

    String title;

    List<QuestionOptionDTO> questionOptions;
}
