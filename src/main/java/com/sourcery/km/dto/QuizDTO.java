package com.sourcery.km.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QuizDTO {
    Long id;

    String title;

    String description;
}
