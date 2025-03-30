package com.sourcery.km.entity;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    // user id
    private UUID created_by;

    private String title;

    private String description;

    private Instant created_at;

    private Instant updated_at;

    private List<Question> questions;
}
