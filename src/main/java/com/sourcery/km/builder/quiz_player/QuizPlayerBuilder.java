package com.sourcery.km.builder.quiz_player;

import com.sourcery.km.dto.quizPlayer.QuizPlayerDTO;
import com.sourcery.km.entity.QuizPlayer;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizPlayerBuilder {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    private void postConstruct() {
        configureDtoToEntity();
        configureEntityToDto();
    }

    private void configureDtoToEntity() {
        PropertyMap<QuizPlayerDTO, QuizPlayer> createMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        modelMapper.addMappings(createMap);
    }

    private void configureEntityToDto() {
        PropertyMap<QuizPlayer, QuizPlayerDTO> createMap = new PropertyMap<>() {
            @Override
            protected void configure() {

            }
        };
        modelMapper.addMappings(createMap);
    }
}