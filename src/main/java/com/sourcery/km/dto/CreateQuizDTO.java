package com.sourcery.km.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateQuizDTO {
    String title;

    String description;
}
