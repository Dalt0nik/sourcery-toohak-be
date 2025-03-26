package com.sourcery.km.dto.quiz;

import com.sourcery.km.entity.Question;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class CreateQuizDTO {
    String title;

    UUID createdBy;

    String description;

    List<Question> questions;

}
