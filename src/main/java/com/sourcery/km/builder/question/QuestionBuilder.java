package com.sourcery.km.builder.question;

import com.sourcery.km.builder.questionOption.QuestionOptionBuilder;
import com.sourcery.km.dto.question.CreateQuestionDTO;
import com.sourcery.km.dto.question.QuestionDTO;
import com.sourcery.km.dto.questionOption.QuestionOptionDTO;
import com.sourcery.km.entity.Question;
import com.sourcery.km.entity.QuestionOption;
import com.sourcery.km.service.MapperService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class QuestionBuilder {

    @Autowired
    private final MapperService mapperService;

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    private void postConstruct() {
        configureMappings();
    }

    private void configureMappings() {
        PropertyMap<CreateQuestionDTO, Question> createQuestionMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        modelMapper.addMappings(createQuestionMap);
    }

    public Question toQuestionEntity(CreateQuestionDTO questionDTO) {
        return Question.builder()
                .title(questionDTO.getTitle())
                .quizId(questionDTO.getQuizId())
                .imageId(questionDTO.getImageId())
                .questionOptions(mapperService.mapList(questionDTO.getQuestionOptions(), QuestionOption.class))
                .build();
    }

    public Question toQuestionEntity(QuestionDTO questionDTO) {
        return Question.builder()
                .id(questionDTO.getId())
                .title(questionDTO.getTitle())
                .imageId(questionDTO.getImageId())
                .questionOptions(mapperService.mapList(questionDTO.getQuestionOptions(), QuestionOption.class))
                .build();
    }

    public QuestionDTO toQuestionDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .quizId(question.getQuizId())
                .imageId(question.getImageId())
                .title(question.getTitle())
                .questionOptions(mapperService.mapList(question.getQuestionOptions(), QuestionOptionDTO.class))
                .build();
    }
}
