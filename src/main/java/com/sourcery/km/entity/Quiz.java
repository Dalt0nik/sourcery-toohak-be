package com.sourcery.km.entity;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    @Builder.Default
    private UUID id = UUID.randomUUID();

    // user id
    private UUID createdBy;

    private String title;

    private String description;

    private List<Question> questions;
}
