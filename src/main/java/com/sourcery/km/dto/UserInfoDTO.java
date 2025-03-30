package com.sourcery.km.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserInfoDTO {
    String sub;
    String name;
    String nickname;
}
