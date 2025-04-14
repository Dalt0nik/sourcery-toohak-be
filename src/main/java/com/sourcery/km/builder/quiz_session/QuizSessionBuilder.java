package com.sourcery.km.builder.quiz_session;

import com.sourcery.km.dto.quizSession.QuizSessionDTO;
import com.sourcery.km.entity.QuizSession;
import com.sourcery.km.entity.QuizStatus;
import com.sourcery.km.util.CodeGenerator;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizSessionBuilder {

    private static final int joinCodeLength = 5;

    @Autowired
    ModelMapper modelMapper;

    public static QuizSession createQuizSession(UUID quizId) {
        return QuizSession.builder()
                .id(UUID.randomUUID())
                .status(QuizStatus.ACTIVE)
                .joinId(CodeGenerator.generateJoinCode(joinCodeLength))
                .quizId(quizId)
                .build();
    }

    @PostConstruct
    private void postConstruct() {
        configureMappings();
    }

    private void configureMappings() {
        PropertyMap<QuizSessionDTO, QuizSession> createMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        modelMapper.addMappings(createMap);
    }
}
