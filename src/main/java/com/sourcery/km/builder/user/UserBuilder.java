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
                .auth0Id(user.getAuth0Id())
                .name(user.getUsername())
                .email(user.getEmail())
                .picture(user.getPicture())
                .build();
    }

    public static User toUserEntity(UserInfoDTO user) {
        return User.builder()
                .id(user.getId())
                .auth0Id(user.getAuth0Id())
                .username(user.getName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .build();
    }
}
