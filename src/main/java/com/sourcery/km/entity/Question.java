package com.sourcery.km.entity;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID quizId;

    private UUID imageId;

    private String title;
}
