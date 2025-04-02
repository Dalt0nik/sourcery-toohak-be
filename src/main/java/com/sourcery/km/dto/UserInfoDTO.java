package com.sourcery.km.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserInfoDTO {
    String sub;

    String name;

    String email;

    UUID id;
}
