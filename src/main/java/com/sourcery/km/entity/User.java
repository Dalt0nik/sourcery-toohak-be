package com.sourcery.km.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Builder.Default
    UUID id = UUID.randomUUID();

    String auth0_id;

    String email;

    String username;

    Instant created_at;

    Instant updated_at;
}
