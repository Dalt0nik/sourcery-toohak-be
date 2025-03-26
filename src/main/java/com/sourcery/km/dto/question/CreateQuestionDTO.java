package com.sourcery.km.dto.question;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateQuestionDTO {
    String title;
}
