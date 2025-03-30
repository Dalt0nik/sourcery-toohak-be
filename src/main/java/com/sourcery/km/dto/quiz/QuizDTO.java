package com.sourcery.km.dto.quiz;

import com.sourcery.km.dto.question.QuestionDTO;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class QuizDTO {
    UUID id;

    String title;

    UUID created_by;

    String description;

    Instant created_at;

    Instant updated_at;

    List<QuestionDTO> questions;
}
