package com.sourcery.km.builder.file;

import com.sourcery.km.dto.file.FileDTO;
import com.sourcery.km.dto.questionOption.CreateQuestionOptionDTO;
import com.sourcery.km.entity.File;
import com.sourcery.km.entity.QuestionOption;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileBuilder {

    @Autowired
    ModelMapper modelMapper;

    @PostConstruct
    private void postConstruct() {
        configureMappings();
    }

    private void configureMappings() {
        PropertyMap<FileDTO, File> createQuestionMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setId(source.getImageId());
            }
        };
        modelMapper.addMappings(createQuestionMap);
    }

    public static File fromFileIdSetTemporary(UUID fileId, boolean isTemporary) {
        return File.builder()
                .id(fileId)
                .isTemporary(isTemporary)
                .build();
    }
}
