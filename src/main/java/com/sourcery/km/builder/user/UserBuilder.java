package com.sourcery.km.builder.user;

import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBuilder {
    public static UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .sub(user.getAuth0_id())
                .name(user.getUsername())
                .email(user.getEmail())
                .picture(user.getPicture())
                .build();
    }

    public static User toUserEntity(UserInfoDTO user) {
        return User.builder()
                .auth0_id(user.getSub())
                .username(user.getName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .build();
    }
}
