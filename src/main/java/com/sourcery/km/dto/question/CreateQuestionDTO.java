package com.sourcery.km.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateQuestionDTO {

    UUID quizId;

    UUID imageId;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 200, message = "Title must be at most 200 characters")
    String title;
}
