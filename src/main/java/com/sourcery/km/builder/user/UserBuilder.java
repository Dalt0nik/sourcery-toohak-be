package com.sourcery.km.builder.user;

import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBuilder {
    public static UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .email(user.getEmail())
                .name(user.getUsername())
                .sub(user.getAuth0_id())
                .build();
    }
}
