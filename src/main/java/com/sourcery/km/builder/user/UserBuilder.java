package com.sourcery.km.builder.user;

import com.sourcery.km.dto.UserInfoDTO;
import com.sourcery.km.dto.questionOption.CreateQuestionOptionDTO;
import com.sourcery.km.entity.QuestionOption;
import com.sourcery.km.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBuilder {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    private void postConstruct() {
        configureMappings();
    }

    private void configureMappings() {
        PropertyMap<UserInfoDTO, User> createQuestionMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setAuth0Id(source.getSub());
                map().setUsername(source.getName());
            }
        };
        modelMapper.addMappings(createQuestionMap);
    }

    public static UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .sub(user.getAuth0Id())
                .name(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
