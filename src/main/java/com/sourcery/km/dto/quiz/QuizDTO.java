package com.sourcery.km.dto.quiz;

import com.sourcery.km.dto.question.QuestionDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class QuizDTO {
    UUID id;

    String title;

    UUID createdBy;

    String description;

    List<QuestionDTO> questions;
}
