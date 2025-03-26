package com.sourcery.km.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    private Long id;

    private String title;

    private String description;
}
